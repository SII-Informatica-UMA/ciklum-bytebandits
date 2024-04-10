import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetalleContactoComponent } from './detalle-contacto.component';
import { Dieta } from '../dieta';

describe('El componente de detalle de dieta', () => {
  let component: DetalleContactoComponent;
  let fixture: ComponentFixture<DetalleContactoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetalleContactoComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetalleContactoComponent);
    component = fixture.componentInstance;
  });

  it('debe mostrar la descripción de la dieta', () => {
    const dieta: Dieta = {
      id: 1,
      nombre: 'Dieta 1',
      descripcion: 'Descripción de la dieta 1',
      observaciones: 'Observaciones de la dieta 1',
      objetivo: 'Objetivo de la dieta 1',
      duracionDias: '30',
      alimentos: ['Alimentos de la dieta 1'],
      recomendaciones: 'Recomendaciones de la dieta 1'
    };
    component.dieta = dieta;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('#Descripcion')?.textContent).toContain(dieta.descripcion);
  });

  it('debe mostrar las observaciones de la dieta', () => {
    const dieta: Dieta = {
      id: 1,
      nombre: 'Dieta 1',
      descripcion: 'Descripción de la dieta 1',
      observaciones: 'Observaciones de la dieta 1',
      objetivo: 'Objetivo de la dieta 1',
      duracionDias: 30,
      alimentos: ['Alimentos de la dieta 1'],
      recomendaciones: 'Recomendaciones de la dieta 1'
    };
    component.dieta = dieta;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('#Observaciones')?.textContent).toContain(dieta.observaciones);
  });

  it('debe mostrar la duración de la dieta', () => {
    const dieta: Dieta = {
      id: 1,
      nombre: 'Dieta 1',
      descripcion: 'Descripción de la dieta 1',
      observaciones: 'Observaciones de la dieta 1',
      objetivo: 'Objetivo de la dieta 1',
      duracionDias: 30,
      alimentos: ['Alimentos de la dieta 1'],
      recomendaciones: 'Recomendaciones de la dieta 1'
    };
    component.dieta = dieta;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('#Duracion')?.textContent).toContain(dieta.duracionDias);
  });

  it('debe mostrar los alimentos de la dieta', () => {
    const dieta: Dieta = {
      id: 1,
      nombre: 'Dieta 1',
      descripcion: 'Descripción de la dieta 1',
      observaciones: 'Observaciones de la dieta 1',
      objetivo: 'Objetivo de la dieta 1',
      duracionDias: 30,
      alimentos: ['Alimentos de la dieta 1'],
      recomendaciones: 'Recomendaciones de la dieta 1'
    };
    component.dieta = dieta;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('#Alimentos')?.textContent).toContain(dieta.alimentos);
  });

  it('debe mostrar las recomendaciones de la dieta', () => {
    const dieta: Dieta = {
      id: 1,
      nombre: 'Dieta 1',
      descripcion: 'Descripción de la dieta 1',
      observaciones: 'Observaciones de la dieta 1',
      objetivo: 'Objetivo de la dieta 1',
      duracionDias: 30,
      alimentos: ['Alimentos de la dieta 1'],
      recomendaciones: 'Recomendaciones de la dieta 1'
    };
    component.dieta = dieta;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('#Recomendaciones')?.textContent).toContain(dieta.recomendaciones);
  });

});


