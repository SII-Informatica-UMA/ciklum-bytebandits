import { ComponentFixture, TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { DietaService } from './dieta.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Dieta } from './dieta';

class MockNgbModal {
  modalRef = {
    componentInstance: {
      dieta: { id: 0, nombre: '', descripcion: '', observaciones: '', objetivo: '', duracionDias: 0, alimentos: '', recomendaciones: '' },
      accion: 'Añadir'
    },
    result: Promise.resolve({ id: 0, nombre: '', descripcion: '', observaciones: '', objetivo: '', duracionDias: 0, alimentos: '', recomendaciones: '' })
  };

  open() {
    return this.modalRef;
  }
}

describe('El componente principal', () => {
  let mockService: DietaService;
  let mockModal: MockNgbModal;
  let fixture: ComponentFixture<AppComponent>;
  let compiled: HTMLElement;

  beforeEach(async () => {
    mockService = {
      getDietas: () => {
        return [
          { id: 1, nombre: 'Dieta1', descripcion: 'Descripción de la dieta 1', observaciones: 'Observaciones de la dieta 1', objetivo: 'Objetivo de la dieta 1', duracionDias: '30', alimentos: 'Alimentos de la dieta 1', recomendaciones: 'Recomendaciones de la dieta 1' },
          { id: 2, nombre: 'Dieta2', descripcion: 'Descripción de la dieta 2', observaciones: 'Observaciones de la dieta 2', objetivo: 'Objetivo de la dieta 2', duracionDias: '30', alimentos: 'Alimentos de la dieta 2', recomendaciones: 'Recomendaciones de la dieta 2' }
        ];
      },
      eliminarDieta: (id: number) => {},
      editarDieta: (dieta: Dieta) => {},
      addDieta: (dieta: Dieta) => {}
    } as DietaService;

    mockModal = new MockNgbModal();

    await TestBed.configureTestingModule({
      declarations: [
        AppComponent
      ],
      providers: [
        { provide: DietaService, useValue: mockService },
        { provide: NgbModal, useValue: mockModal }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    fixture.detectChanges();
    compiled = fixture.nativeElement as HTMLElement;
  });

  it('debe mostrar todas las dietas en la lista de botones', () => {
    const buttons = compiled.querySelectorAll('.list-group button');
    expect(buttons.length).toBe(2);
    expect(buttons[0].textContent).toContain('Dieta1');
    expect(buttons[1].textContent).toContain('Dieta2');
  });


  it('el botón de añadir debe abrir el formulario vacío', () => {
    const button = compiled.querySelector('.btn-outline-primary.bi-plus-lg.mb-4') as HTMLElement;
    const spyOpen = spyOn(mockModal, 'open').and.callThrough();
    button.click();
    expect(spyOpen).toHaveBeenCalled();
  });
});

