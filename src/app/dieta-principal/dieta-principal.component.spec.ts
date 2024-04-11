import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DietaPrincipalComponent } from './dieta-principal.component';

describe('DietaPrincipalComponent', () => {
  let component: DietaPrincipalComponent;
  let fixture: ComponentFixture<DietaPrincipalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [DietaPrincipalComponent]
    }).compileComponents();
    
    fixture = TestBed.createComponent(DietaPrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have dieta array initialized', () => {
    expect(component.dieta).toBeDefined();
    expect(component.dieta.length).toBe(0);
  });

  it('should have dietaElegida undefined', () => {
    expect(component.dietaElegida).toBeUndefined();
  });

  it('should select a dieta', () => {
    const dieta = {id: 1, nombre: 'Dieta 1', descripcion:'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', observaciones: 'Ninguna', objetivo: 'Aumentar masa muscular', duracionDias: 30, alimentos: ['Arroz', 'Pollo'], recomendaciones: 'Ninguna', idCliente: 1 , idEntrenador:1}; 
    component.elegirDieta(dieta);
    expect(component.dietaElegida).toEqual(dieta);
  });

  it('should add a dieta', () => {
    const initialLength = component.dieta.length;
    component.aniadirDieta();
    expect(component.dieta.length).toBe(initialLength + 1);
  });

  it('should edit a dieta', () => {
    const dieta = {id: 1, nombre: 'Dieta 1', descripcion:'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', observaciones: 'Ninguna', objetivo: 'Aumentar masa muscular', duracionDias: 30, alimentos: ['Arroz', 'Pollo'], recomendaciones: 'Ninguna', idCliente: 1 , idEntrenador:1}; 
    component.dieta = [dieta];
    component.dietaElegida = dieta;
    const newDescripcion = 'Nueva descripción';
    dieta.descripcion = newDescripcion;
    component.dietaEditada(dieta);
    expect(component.dieta[0].descripcion).toBe(newDescripcion);
  });

  it('should delete a dieta', () => {
    const dieta = {id: 1, nombre: 'Dieta 1', descripcion:'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', observaciones: 'Ninguna', objetivo: 'Aumentar masa muscular', duracionDias: 30, alimentos: ['Arroz', 'Pollo'], recomendaciones: 'Ninguna', idCliente: 1 , idEntrenador:1}; 
    component.dieta = [dieta];
    component.dietaElegida = dieta;
    component.eliminarDieta(dieta.id);
    expect(component.dieta.length).toBe(0);
    expect(component.dietaElegida).toBeUndefined();
  });


});
