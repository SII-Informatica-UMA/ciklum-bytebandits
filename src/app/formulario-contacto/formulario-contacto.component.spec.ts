import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { FormularioContactoComponent } from './formulario-contacto.component';
import { FormsModule } from '@angular/forms';

describe('El formulario de dietas', () => {
  let component: FormularioContactoComponent;
  let fixture: ComponentFixture<FormularioContactoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FormsModule],
      declarations: [FormularioContactoComponent],
      providers: [NgbActiveModal]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FormularioContactoComponent);
    component = fixture.componentInstance;
  });

  it('debe mostrar el nombre de la dieta', () => {
    component.accion = "Añadir";
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('label[for="nombre"]')).not.toBeNull();
    expect(compiled.querySelector('label[for="nombre"]')?.textContent).toContain('Nombre:');
    expect(compiled.querySelector('input#nombre')).not.toBeNull();
  });


  it('debe cambiar el modelo cuando se modifica el nombre de la dieta', (done: DoneFn) => {
    component.accion = "Añadir";
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const inputNombre = compiled.querySelector('input#nombre') as HTMLInputElement;
    const nuevoNombre = 'Nueva Dieta';

    inputNombre.value = nuevoNombre;
    inputNombre.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    fixture.whenStable().then(() => {
      expect(component.dieta.nombre).toBe(nuevoNombre);
      done();
    });
  });

  it('debe cambiar el modelo cuando se modifica la descripción de la dieta', (done: DoneFn) => {
    component.accion = "Añadir";
    fixture.detectChanges();
  
    const compiled = fixture.nativeElement as HTMLElement;
    const inputDescripcion = compiled.querySelector('input#descripcion') as HTMLInputElement;
    const nuevaDescripcion = 'Nueva descripción para la dieta';
  
    inputDescripcion.value = nuevaDescripcion;
    inputDescripcion.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  
    fixture.whenStable().then(() => {
      expect(component.dieta.descripcion).toBe(nuevaDescripcion);
      done();
    });
  });
  
  it('debe cambiar el modelo cuando se modifica el objetivo de la dieta', (done: DoneFn) => {
    component.accion = "Añadir";
    fixture.detectChanges();
  
    const compiled = fixture.nativeElement as HTMLElement;
    const inputObjetivo = compiled.querySelector('input#objetivo') as HTMLInputElement;
    const nuevoObjetivo = 'Perder peso';
  
    inputObjetivo.value = nuevoObjetivo;
    inputObjetivo.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  
    fixture.whenStable().then(() => {
      expect(component.dieta.objetivo).toBe(nuevoObjetivo);
      done();
    });
  });
  
  it('debe cambiar el modelo cuando se modifica la observaciones de la dieta', (done: DoneFn) => {
    component.accion = "Añadir";
    fixture.detectChanges();
  
    const compiled = fixture.nativeElement as HTMLElement;
    const inputObservaciones = compiled.querySelector('input#observaciones') as HTMLInputElement;
    const nuevasObservaciones = 'Nuevas observaciones para la dieta';
  
    inputObservaciones.value = nuevasObservaciones;
    inputObservaciones.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  
    fixture.whenStable().then(() => {
      expect(component.dieta.observaciones).toBe(nuevasObservaciones);
      done();
    });
  });
  
  it('debe cambiar el modelo cuando se modifica la duración en días de la dieta', (done: DoneFn) => {
    component.accion = "Añadir";
    fixture.detectChanges();
  
    const compiled = fixture.nativeElement as HTMLElement;
    const inputDuracionDias = compiled.querySelector('input#duracionDias') as HTMLInputElement;
    const nuevaDuracionDias = 10;
  
    inputDuracionDias.value = nuevaDuracionDias.toString();
    inputDuracionDias.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  
    fixture.whenStable().then(() => {
      expect(component.dieta.duracionDias).toBe(nuevaDuracionDias);
      done();
    });
  });
  
  it('debe cambiar el modelo cuando se modifica la lista de alimentos de la dieta', (done: DoneFn) => {
    component.accion = "Añadir";
    fixture.detectChanges();
  
    const compiled = fixture.nativeElement as HTMLElement;
    const inputAlimentos = compiled.querySelector('input#alimentos') as HTMLInputElement;
    const nuevosAlimentos = 'Nuevos alimentos para la dieta';
  
    inputAlimentos.value = nuevosAlimentos;
    inputAlimentos.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  
    fixture.whenStable().then(() => {
      expect(component.dieta.alimentos).toBe(nuevosAlimentos);
      done();
    });
  });
  
  it('debe cambiar el modelo cuando se modifica las recomendaciones de la dieta', (done: DoneFn) => {
    component.accion = "Añadir";
    fixture.detectChanges();
  
    const compiled = fixture.nativeElement as HTMLElement;
    const inputRecomendaciones = compiled.querySelector('input#recomendaciones') as HTMLInputElement;
    const nuevasRecomendaciones = 'Nuevas recomendaciones para la dieta';
  
    inputRecomendaciones.value = nuevasRecomendaciones;
    inputRecomendaciones.dispatchEvent(new Event('input'));
    fixture.detectChanges();
  
    fixture.whenStable().then(() => {
      expect(component.dieta.recomendaciones).toBe(nuevasRecomendaciones);
      done();
    });
  });
  
});

