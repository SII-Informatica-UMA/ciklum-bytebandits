import { Component, OnInit } from '@angular/core';
import {Dieta } from '../dieta';
import { DietaService } from '../dieta.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {FormularioDietaComponent} from '../formulario-dieta/formulario-dieta.component'
import {DetalleDietaComponent} from '../detalle-dieta/detalle-dieta.component'
import { RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterLink, FormsModule, CommonModule, DetalleDietaComponent],
  templateUrl: './dieta-principal.component.html',
  styleUrls: ['./dieta-principal.component.css']
})

export class DietaPrincipalComponent {
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

