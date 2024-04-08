import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DietaPrincipalComponent } from './dieta-principal.component';

describe('DietaPrincipalComponent', () => {
  let component: DietaPrincipalComponent;
  let fixture: ComponentFixture<DietaPrincipalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DietaPrincipalComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(DietaPrincipalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
