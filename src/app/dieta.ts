import { Usuario } from "./entities/usuario";
export interface Dieta {
  id: number;
  nombre: string;
  descripcion: string;
  observaciones: string;
  objetivo: string;
  duracionDias: number|null;
  alimentos: string[];
  recomendaciones: string;
  idCliente: number|null;
  idEntrenador: number|null;
}
