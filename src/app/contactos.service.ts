import { Injectable } from '@angular/core';
import {Contacto } from './contacto';

@Injectable({
  providedIn: 'root'
})

export class ContactosService {
  private contactos: Contacto [] = [
    {id: 1, nombre: 'Dieta1', apellidos:'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', email: 'perez@uma.es', telefono: '666666666'},
    {id: 2, nombre: 'Dieta2', apellidos: 'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', email: 'ana@uma.es', telefono: '55555555'},
    {id: 3, nombre: 'Dieta3', apellidos: 'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', email: 'gonzalez@uma.es', telefono: '444444444'},
  ];

  constructor() { }

  getContactos(): Contacto [] {
    return this.contactos;
  }

  addContacto(contacto: Contacto) {
    contacto.id = Math.max(...this.contactos.map(c => c.id)) + 1;
    this.contactos.push(contacto);
  }

  editarContacto(contacto: Contacto) {
    let indice = this.contactos.findIndex(c => c.id == contacto.id);
    this.contactos[indice] = contacto;
  }

  eliminarContacto(id: number) {
    let indice = this.contactos.findIndex(c => c.id == id);
    this.contactos.splice(indice, 1);
  }
}
