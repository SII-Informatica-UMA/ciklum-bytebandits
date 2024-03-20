import { FormularioContactoComponent } from './formulario-contacto/formulario-contacto.component';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import {ContactosService } from './contactos.service';
import {NgbModal} from '@ng-bootstrap/ng-bootstrap';
import {Contacto} from './contacto';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';

class MockNgbModal {
  modalRef = {
    componentInstance: {
      contacto: {id: 0, nombre: '', apellidos: '', email: '', telefono: ''},
      accion: 'Añadir'},
    result: Promise.resolve({id: 0, nombre: '', apellidos: '', email: '', telefono: ''})};

  open() {
    return this.modalRef;
  }
}

describe('El componente principal', () => {
  let mockService: ContactosService;
  let mockModal: MockNgbModal;
  let fixture: ComponentFixture<AppComponent>;
  let compiled: HTMLElement;
  let eddd=0

  beforeEach(async () => {
    mockService = {
      getContactos: () => {
        return [
          {id: 1, nombre: 'Dieta1', apellidos: 'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', email: '', telefono: ''},
          {id: 2, nombre: 'Dieta2', apellidos: 'Una dieta equilibrada incluye una variedad de alimentos nutritivos como proteínas magras, carbohidratos complejos, grasas saludables y vitaminas/minerales, evitando el exceso de azúcares y alimentos procesados. Es crucial ajustar las porciones según las necesidades individuales y mantener un equilibrio entre la ingesta de calorías y el gasto energético.', email: '', telefono: ''}]
      },
      eliminarContacto: (id: number) => {},
      editarContacto: (contacto: Contacto) => {eddd+=1},
      addContacto: (contacto: Contacto) => {}
    } as ContactosService;

    mockModal = new MockNgbModal();

    await TestBed.configureTestingModule({
      declarations: [
        AppComponent,
        FormularioContactoComponent
      ],
      imports: [BrowserModule,
        FormsModule],
      providers: [
        {provide: ContactosService, useValue: mockService},
        {provide: NgbModal, useValue: mockModal}]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    compiled = fixture.nativeElement as HTMLElement;
  });

  it('debe tener una tabla', () => {
    expect(compiled.querySelectorAll('table')).toHaveSize(1);
  });

  it('debe mostrar a Ana en primer lugar en la tabla', () => {
    expect(compiled.querySelector('tbody tr:first-child td:first-child')?.textContent).toBe('Ana');
  });

  it('debe mostrar a Juan en segundo lugar en la lista de botones', () => {
    expect(compiled.querySelector('tbody tr:nth-child(2) td:first-child')?.textContent).toBe('Juan');
  });

  it('debe haber dos botones por fila', () => {
    expect(compiled.querySelectorAll('tbody tr').length).not.toBe(0);
    compiled.querySelectorAll('tbody tr').forEach((fila) => {
      expect(fila.querySelectorAll('td button')).toHaveSize(2);
    });
  });

  it('el segundo botón debe eliminar un contacto', (done: DoneFn) => {
    let spy = spyOn(mockService, 'eliminarContacto').and.callThrough();
    const botonEliminar = compiled.querySelector('tbody tr:nth-child(1) td:last-child button:last-child') as HTMLElement
    botonEliminar.click();
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(spy).toHaveBeenCalled();
      done();
    });
  });

  it('el primer botón debe abrir el formulario para editar el contacto', (done: DoneFn) => {
    const botonEditar = compiled.querySelector('tbody tr:nth-child(1) td:last-child button:nth-last-child(2)') as HTMLElement
    mockModal.modalRef.result = Promise.resolve({id: 1, nombre: 'Juan', apellidos: 'Pérez', email: 'perez@uma.es', telefono: '1234'});
    const spyEditar = spyOn(mockService, 'editarContacto').and.callThrough();

    botonEditar.click();
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(spyEditar).toHaveBeenCalled();
      done();
    });
  });

  it('debe haber un botón para añadir un contacto', () => {
    expect(compiled.querySelector('table ~ button')).not.toBeNull();
  });

  it('el botón para añadir debe abrir el formulario vacío', (done: DoneFn) => {
    const botonAniadir = compiled.querySelector('table ~ button') as HTMLElement
    mockModal.modalRef.result = Promise.resolve({id: 3, nombre: 'Antonio', apellidos: 'García', email: '', telefono: ''});
    const spyAniadir = spyOn(mockService, 'addContacto').and.callThrough();

    botonAniadir.click();
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      expect(spyAniadir).toHaveBeenCalled();
      done();
    });

  });

  it('al insertar un nuevo contacto se debe añadir en su lugar de acuerdo con el orden por nombre', (done: DoneFn) => {
    let carla: Contacto = {id: 3, nombre: 'Carla', apellidos: 'Fernández', email: '', telefono: ''};
    const spyEditar = spyOn(mockService, 'getContactos').and.returnValue([
      {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: ''},
      {id: 2, nombre: 'Ana', apellidos: 'García', email: '', telefono: ''},
      carla
    ])

    const botonAniadir = compiled.querySelector('table ~ button') as HTMLElement
    mockModal.modalRef.result = Promise.resolve(carla);

    botonAniadir.click();
    fixture.detectChanges();
    fixture.whenStable().then(() => {
      fixture.detectChanges();
      expect(compiled.querySelector('tbody tr:nth-child(2) td:first-child')?.textContent).toBe('Carla');
      done();
    });
  });

});
