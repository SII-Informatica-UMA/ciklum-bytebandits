import { Usuario } from "./entities/usuario";
export interface Dieta {
  id: number;
  nombre: string;
  descripcion: string;
  observaciones: string;
  objetivo: string;
  duracionDias: string;
  alimentos: string;
  recomendaciones: string;
  usuarioAsociado:Usuario
}
