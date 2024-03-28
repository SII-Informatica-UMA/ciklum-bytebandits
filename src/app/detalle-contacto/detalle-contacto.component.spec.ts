import { ComponentFixture, TestBed } from '@angular/core/testing';
import { DetalleContactoComponent } from './detalle-contacto.component';


describe('El componente de detalle de contacto', () => {
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

  it('debe mostrar el atributo "género" debajo del teléfono', () => {
    component.contacto = {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: '', genero: 'Hombre'};
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('#telefono + div')).not.toBeNull();
    expect(compiled.querySelector('#telefono + div label')?.textContent).toContain('Género');
  });

  it('debe mostrar el género para los contactos (probando con Mujer)', () => {
    component.contacto = {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: '', genero: 'Mujer'};
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('#telefono + div')).not.toBeNull();
    expect(compiled.querySelector('#telefono + div span')?.textContent).toContain('Mujer');
  });

  it('debe mostrar el género para los contactos (probando con Hombre)', () => {
    component.contacto = {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: '', genero: 'Hombre'};
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('#telefono + div')).not.toBeNull();
    expect(compiled.querySelector('#telefono + div span')?.textContent).toContain('Hombre');
  });

  it('debe mostrar el género para los contactos (probando con la cadena vacía)', () => {
    component.contacto = {id: 1, nombre: 'Juan', apellidos: 'Pérez', email: '', telefono: '', genero: ''};
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('#telefono + div')).not.toBeNull();
    expect(compiled.querySelector('#telefono + div span')?.textContent).toEqual('');
  });

});
