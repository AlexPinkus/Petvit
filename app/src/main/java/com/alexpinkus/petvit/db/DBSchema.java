package com.alexpinkus.petvit.db;

/**
 * Created by Rodrigo on 19/04/17.
 */

public final class DBSchema {

    public static final class PetTable{
        public static final String NAME = "pet";

        public static final class Columns {
            public static final String ID = "id";
            public static final String NOMBRE = "nombre";
            public static final String EDAD = "edad";
            public static final String PESO = "peso";
            public static final String RAZA_ID = "raza_id";
            public static final String FOTO = "foto";
        }
    }

    public static final class RazasTable{
        public static final String NAME = "razas";

        public static final class Columns {
            public static final String ID = "id";
            public static final String TIPO_RAZA = "tipo_raza";
            public static final String NOMBRE_RAZA = "nombre_raza";
            public static final String PESO_ADULTO = "peso_adulto";
            public static final String ALTURA_ADULTO = "altura_adulto";
            public static final String DESCRIPCION = "descripcion";
            public static final String ESPERANZA = "esperanza";
            public static final String ORIGEN = "origen";
            public static final String HISTORIA = "historia";
            public static final String FOTO = "foto";
        }
    }

    public static final class PorcionesTable{
        public static final String NAME = "porciones";

        public static final class Columns {
            public static final String ID = "id";
            public static final String RAZA_ID = "raza_id";
            public static final String PESO_PET = "peso_pet";
            public static final String EDAD_PET = "edad_pet";
            public static final String TIPO_RAZA = "tipo_raza_";
            public static final String GRAMOS = "gramos";
        }
    }

}
