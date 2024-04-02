import { Component, OnInit } from '@angular/core';
import {Dieta } from './dieta';
import {DietaService } from './dieta.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioDietaComponent} from './formulario-dieta/formulario-dieta.component'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent implements OnInit {
  dieta: Dieta [] = [];
  dietaElegida?: Dieta;

  constructor(private dietaService: DietaService, private modalService: NgbModal) { }

  ngOnInit(): void {
    this.dieta = this.dietaService.getDieta();
  }

  elegirDieta(dieta: Dieta): void {
    this.dietaElegida = dieta;
  }

  aniadirDieta(): void {
    let ref = this.modalService.open(FormularioDietaComponent);
    ref.componentInstance.accion = "Añadir";
    ref.componentInstance.dieta = {id: 0, nombre: '', descripcion: '', observaciones: '', objetivo: '', duracionDias: '', alimentos: '', recomendaciones: ''};
    ref.result.then((dieta: Dieta) => {
      this.dietaService.addDieta(dieta);
      this.dieta = this.dietaService.getDieta();
    }, (reason) => {});

  }
  dietaEditada(dieta: Dieta): void {
    this.dietaService.editarDieta(dieta);
    this.dieta = this.dietaService.getDieta();
    this.dietaElegida = this.dieta.find(c => c.id == dieta.id);
  }

  eliminarDieta(id: number): void {
    this.dietaService.eliminarDieta(id);
    this.dieta = this.dietaService.getDieta();
    this.dietaElegida = undefined;
  }
}
