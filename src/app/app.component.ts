import { Component, OnInit } from '@angular/core';
import {Dieta } from './dieta';
import {DietaService } from './dieta.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioContactoComponent} from './formulario-contacto/formulario-contacto.component'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  dietas: Dieta [] = [];
  dietaElegida?: Dieta;

  constructor(private dietaService: DietaService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.dietas = this.dietaService.getDietas();
  }

  elegirDieta(dieta: Dieta): void {
    this.dietaElegida = dieta;
  }

  aniadirDieta(): void {
    let ref = this.modalService.open(FormularioContactoComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.dieta = {id: 0, nombre: '', descripcion: '', observaciones: '', objetivo: '', duracionDias: 0, alimentos: '', recomendaciones: ''};
    ref.result.then((dieta: Dieta) => {
      this.dietaService.addDieta(dieta);
      this.dietas = this.dietaService.getDietas();
    }, (reason) => {});

  }
  dietaEditada(dieta: Dieta): void {
    this.dietaService.editarDieta(dieta);
    this.dietas = this.dietaService.getDietas();
    this.dietaElegida = this.dietas.find(c => c.id == dieta.id);
  }

  eliminarDieta(id: number): void {
    this.dietaService.eliminarDieta(id);
    this.dietas = this.dietaService.getDietas();
    this.dietaElegida = undefined;
  }
}
