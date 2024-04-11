import { Component } from '@angular/core';
import { Dieta } from '../dieta';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { usuariosC } from '../services/backend.fake.service';
import { Usuario } from '../entities/usuario';
import { UsuariosService } from '../services/usuarios.service';

@Component({
  selector: 'app-formulario-dieta',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './formulario-dieta.component.html',
  styleUrls: ['./formulario-dieta.component.css']
})
export class FormularioDietaComponent {

  accion?: "Añadir" | "Editar";
  dieta: Dieta = {id: 0, nombre: '', descripcion: '', observaciones: '', objetivo: '', duracionDias: null, alimentos: [], recomendaciones: '', idCliente: null, idEntrenador: null};


  constructor(private usuariosService: UsuariosService,public modal: NgbActiveModal) {
    //this.actualizarUsuarios();

   }


   /*onUsuarioSeleccionado(usuarioId: number): void {
    // Opcional: Puedes realizar validaciones o lógica adicional aquí
    const usuarioSeleccionado = this.usuarios.find(usuario => usuario.id === usuarioId);
    if (usuarioSeleccionado) {
      this.usuariosSeleccionados = [usuarioSeleccionado]; // Actualiza el array con el usuario seleccionado
    } else {
      this.usuariosSeleccionados = []; // Vacía el array si se deselecciona
    }
  }*/
  

  guardarDieta(): void {
    this.modal.close(this.dieta);
    console.log(this.dieta.idCliente);
  }

  getUsuarios(){
    //this.actualizarUsuarios();

    return this.dieta.idCliente;
  }
 /*actualizarUsuarios() {
    this.usuariosService.getUsuarios().subscribe(usuarios => {
      this.usuarios = usuarios;
    });
  }*/
}
