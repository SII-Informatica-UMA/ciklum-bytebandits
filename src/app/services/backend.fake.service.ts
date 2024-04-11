import { Injectable } from "@angular/core";
import { Observable, of } from "rxjs";
import { Usuario } from "../entities/usuario";
import { SECRET_JWT } from "../config/config";
import { from } from "rxjs";
import * as jose from 'jose';
import { FRONTEND_URI } from "../config/config";
import { Dieta } from "../dieta";

// Este servicio imita al backend pero utiliza localStorage para almacenar los datos

export const usuariosC: Usuario [] = [
  {
    id: 1,
    nombre: 'Admin',
    apellido1: 'Admin',
    apellido2: 'Admin',
    email: 'admin@uma.es',
    administrador: true,
    password: 'admin'
  },
  {
    id: 2,
    nombre: 'Antonio',
    apellido1: 'García',
    apellido2: 'Ramos',
    email: 'antonio@uma.es',
    administrador: false,
    password: '5678'
  },
  {
    id: 3,
    nombre: 'Juan',
    apellido1: 'García',
    apellido2: 'Ramos',
    email: 'juan@uma.es',
    administrador: false,
    password: '5678'
  },
  {
    id: 4,
    nombre: 'Alba',
    apellido1: 'García',
    apellido2: 'Ramos',
    email: 'albondiga@uma.es',
    administrador: true,
    password: '5678'
  },
  {
    id: 5,
    nombre: 'Carmen',
    apellido1: 'García',
    apellido2: 'Ramos',
    email: '33@uma.es',
    administrador: false,
    password: '5678'
  },
];

const dietasC: Dieta[] = [
  {id: 1, nombre: 'Dieta 1', descripcion:'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', observaciones: 'Ninguna', objetivo: 'Aumentar masa muscular', duracionDias: 30, alimentos: ['Arroz', 'Pollo'], recomendaciones: 'Ninguna', idCliente: 1 , idEntrenador:1},
]

@Injectable({
  providedIn: 'root'
})
export class BackendFakeService {
  private usuarios: Usuario [];
  private dietas: Dieta [];
  private forgottenPasswordTokens;

  constructor() {
    let _usuarios = localStorage.getItem('usuarios');
    if (_usuarios) {
      this.usuarios = JSON.parse(_usuarios);
    } else {
      this.usuarios = [...usuariosC];
    }

    let _forgottenPasswordTokens = localStorage.getItem('forgottenPasswordTokens');
    if (_forgottenPasswordTokens) {
      this.forgottenPasswordTokens = new Map(JSON.parse(_forgottenPasswordTokens));
    } else {
      this.forgottenPasswordTokens = new Map();
    }

    let _dietas = localStorage.getItem('dietas');
    if(_dietas) {
      this.dietas = JSON.parse(_dietas);
    } else {
      this.dietas = [...dietasC];
    }

  }

  getUsuarios(): Observable<Usuario[]> {
    return of(this.usuarios);
  }

  postUsuario(usuario: Usuario): Observable<Usuario> {
    let u = this.usuarios.find(u => u.email == usuario.email);
    if (!usuario.email) {
      return new Observable<Usuario>(observer => {
        observer.error('El email es obligatorio');
      });
    }
    if (u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario ya existe');
      });
    }
    // Si no trae contraseña generamos una aleatoria
    if (usuario.password.length == 0) {
      usuario.password = this.generarCadena();
    }

    usuario.id = this.usuarios.map(u => u.id).reduce((a, b) => Math.max(a, b)) + 1;
    this.usuarios.push(usuario);
    this.guardarUsuariosEnLocalStorage();
    return of(usuario);
  }

  private guardarUsuariosEnLocalStorage() {
    localStorage.setItem('usuarios', JSON.stringify(this.usuarios));
  }

  private guardarForgottenPasswordTokensEnLocalStorage() {
    localStorage.setItem('forgottenPasswordTokens', JSON.stringify(Array.from(this.forgottenPasswordTokens.entries())));
  }

  putUsuario(usuario: Usuario): Observable<Usuario> {
    let u = this.usuarios.find(u => u.id == usuario.id);
    if (!u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario no existe');
      });
    }
    // Si la contraseña está en blanco mantenemos la que ya tiene
    if (usuario.password.length == 0) {
      usuario.password = u.password;
    }

    Object.assign(u, usuario);
    this.guardarUsuariosEnLocalStorage();
    return of(u);
  }

  deleteUsuario(id: number): Observable<void> {
    let i = this.usuarios.findIndex(u => u.id == id);
    if (i < 0) {
      return new Observable<void>(observer => {
        observer.error('El usuario no existe');
      });
    }
    this.usuarios.splice(i, 1);
    this.guardarUsuariosEnLocalStorage();
    return of();
  }

  getUsuario(id: number): Observable<Usuario> {
    let u = this.usuarios.find(u => u.id == id);
    if (!u) {
      return new Observable<Usuario>(observer => {
        observer.error('El usuario no existe');
      });
    }
    return of(u);
  }

  login(email: string, password: string): Observable<string> {
    let u = this.usuarios.find(u => u.email == email && u.password == password);
    if (!u) {
      return new Observable<string>(observer => {
        observer.error({status: 401, statusText: 'Usuario o contraseña incorrectos'});
      });
    }
    return from(this.generateJwt(u));
  }

  forgottenPassword(email: string): Observable<void> {
    const token = this.generarCadena()
    console.log('Para resetar la contraseña acceda a: '+FRONTEND_URI+'/reset-password?token='+token);
    this.forgottenPasswordTokens.set(token, email);
    this.guardarForgottenPasswordTokensEnLocalStorage();
    return of();
  }

  resetPassword(token: string, password: string): Observable<void> {
    if (!this.forgottenPasswordTokens.get(token)) {
      return new Observable<void>(observer => {
        observer.error('Token incorrecto');
      });
    }
    let email = this.forgottenPasswordTokens.get(token);
    console.log("Email for token: ", email)
    let u = this.usuarios.find(u => u.email == email);
    if (!u) {
      return new Observable<void>(observer => {
        observer.error('Usuario no existe');
      });
    }
    u.password = password;
    this.forgottenPasswordTokens.delete(token);

    this.guardarUsuariosEnLocalStorage();
    this.guardarForgottenPasswordTokensEnLocalStorage();
    return of();
  }

  private generateJwt(usuario: Usuario): Promise<string> {
    const secret = new TextEncoder().encode(SECRET_JWT);
    return new jose.SignJWT({ sub: ""+usuario.id, email: usuario.email })
      .setProtectedHeader({ alg: 'HS256' })
      .sign(secret);
  }

  private generarCadena(): string {
    return Math.random().toString(36).substring(2);
  }

  //--------------------------------------------------------------------------------------------------------


/*  getDietas(): Observable<Dieta[]> {
    return of(this.dietas);
  }

  getDietasByClientId (idCliente: number): Observable<Dieta[]> {
    let dietasCliente: Dieta [] = [];
    console.log('HOLA desde backend.service.ts');  
    if (idCliente != -1) {
      this.dietas.forEach((dieta: Dieta) => {
        if (dieta.clientes.includes(idCliente)) {
          dietasCliente.push(dieta);
        }
      });
    }
    return of (dietasCliente);
  }
  
  getDietaByUserId(userId: number): Dieta {
      const dieta = this.dietas.find(d => d.clientes && d.clientes.includes(userId));
      return dieta as Dieta;
  }

  postDieta(idEntrenador: number, dieta: Dieta): Observable<Dieta> {
    let u = this.dietas.find(d => d.nombre == dieta.nombre);
    if (!dieta.nombre) {
      return new Observable<Dieta>(observer => {
        observer.error('El nombre es obligatorio');
      });
    }
    if (u) {
      return new Observable<Dieta>(observer => {
        observer.error('Ya hay una dieta con ese nombre');
      });
    }

    dieta.id = this.dietas.map(u => u.id).reduce((a, b) => Math.max(a, b)) + 1;
    dieta.idEntrenador = idEntrenador;
    this.dietas.push(dieta);
    this.guardarDietasEnLocalStorage();
    return of(dieta);
  } 

  private guardarDietasEnLocalStorage() {
    localStorage.setItem('dietas', JSON.stringify(this.dietas));
  }

  putDieta(dieta: Dieta): Observable<Dieta> {
    let d = this.dietas.find(d => d.id == dieta.id);
    if (!d) {
      return new Observable<Dieta>(observer => {
        observer.error('La dieta no existe');
      });
    }

    Object.assign(d, dieta);
    this.guardarDietasEnLocalStorage();
    return of(d);
  }


  deleteDieta(id: number): Observable<void> {
    let i = this.dietas.findIndex(d => d.id == id);
    if (i < 0)  {
      return new Observable<void>(observer => {
        observer.error('La dieta no existe');
      });
    }
    this.dietas.splice(i, 1);
    this.guardarDietasEnLocalStorage();
    return of();
  }
*/
}
