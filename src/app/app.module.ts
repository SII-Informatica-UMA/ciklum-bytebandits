import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DetalleDietaComponent } from './detalle-dieta/detalle-dieta.component';
import { FormularioDietaComponent } from './formulario-dieta/formulario-dieta.component';

@NgModule({
  declarations: [
    AppComponent,
    DetalleDietaComponent,
    FormularioDietaComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgbModule,
    FormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
