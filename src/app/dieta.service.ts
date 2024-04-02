import { Injectable } from '@angular/core';
import {Dieta } from './dieta';

@Injectable({
  providedIn: 'root'
})

export class DietaService {
  private dieta: Dieta [] = [
    {id: 1, nombre: 'Dieta 1', descripcion:'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', observaciones: 'Ninguna', objetivo: 'Aumentar masa muscular', duracionDias: '30', alimentos: 'Arroz, Pollo', recomendaciones: 'Ninguna'},
    {id: 2, nombre: 'Dieta 2', descripcion:'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', observaciones: 'Ninguna', objetivo: 'Aumentar masa muscular', duracionDias: '30', alimentos: 'Arroz, Pollo', recomendaciones: 'Ninguna'},
    {id: 3, nombre: 'Dieta 3', descripcion:'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', observaciones: 'Ninguna', objetivo: 'Aumentar masa muscular', duracionDias: '30', alimentos: 'Arroz, Pollo', recomendaciones: 'Ninguna'},
  ];

  constructor() { }

  getDieta(): Dieta [] {
    return this.dieta;
  }

  addDieta(dieta: Dieta) {
    dieta.id = Math.max(...this.dieta.map(c => c.id)) + 1;
    this.dieta.push(dieta);
  }

  editarDieta(dieta: Dieta) {
    let indice = this.dieta.findIndex(c => c.id == dieta.id);
    this.dieta[indice] = dieta;
  }

  eliminarDieta(id: number) {
    let indice = this.dieta.findIndex(c => c.id == id);
    this.dieta.splice(indice, 1);
  }
}
