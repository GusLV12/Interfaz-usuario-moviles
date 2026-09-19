import 'package:flutter/material.dart';

enum CatalogSection {
  home(
    'Catálogo UI',
    'Explora componentes móviles y sus demostraciones interactivas.',
    Icons.home_outlined,
    'Inicio',
  ),
  text(
    'Entrada de texto',
    'Campos, validación, contraseñas, teclados y búsqueda.',
    Icons.text_fields_outlined,
    'Entrada',
  ),
  actions(
    'Botones y acciones',
    'Acciones con estados y respuestas visibles.',
    Icons.touch_app_outlined,
    'Acciones',
  ),
  selection(
    'Elementos de selección',
    'Opciones, interruptores, rangos y selectores.',
    Icons.tune_outlined,
    'Selección',
  ),
  collections(
    'Listas y colecciones',
    'Listas, cuadrículas, gestos y pestañas.',
    Icons.list_alt_outlined,
    'Listas',
  ),
  feedback(
    'Información y retroalimentación',
    'Mensajes, progreso, imágenes y diálogos.',
    Icons.info_outline,
    'Información',
  ),
  layout(
    'Contenedores y estructura',
    'Filas, columnas, superposiciones y navegación.',
    Icons.dashboard_outlined,
    'Estructura',
  );

  const CatalogSection(
    this.title,
    this.description,
    this.icon,
    this.shortTitle,
  );

  final String title;
  final String description;
  final IconData icon;
  final String shortTitle;
}

const bottomSections = [
  CatalogSection.text,
  CatalogSection.selection,
  CatalogSection.collections,
  CatalogSection.feedback,
];
