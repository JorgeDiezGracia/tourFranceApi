Actividad de Aprendizaje de la 1ª y 2ª Evaluación de la asignatura Acceso a Datos.

Puntos desarrollados en 1ª evaluación:
- Diseño de APIA y fichero OpenAPI 3.0
- Modelo de datos compuesto por 5 clases cumpliendo con los requisitos mínimos estipulados
- CRUD completo
- Opción de filtrado en los GET de cada clase con 3 campos diferentes
- Colección Hoppscotch 
- Uso de GitFlow
- Log que registra las trazabilidades de todas las operaciones así como de los errores


Consiste en una API denominada tourFrance a través de la cual se puede consultar información sobre equipos, ciclistas, patrocinadores, etapas y puertos de montaña.

Antes de acceder a la API, recomiendo acceder al fichero OpenAPI 3.0 en el que se explica de manera clara y sencilla todas las operaciones para cada clase, así como errores.

Una vez consultado, a través del comando 'mvn-spring-boot:run' en consola se arranca la API.

Mediante la aplicación de escritorio Hoppscotch, se puede acceder a la colección 'mockTourFrance' en la que se encuentran las requests de cada clase con los diferentes casos de uso (200, 201, 400, 404, 500..).  Es importante acceder a ella a través del entorno 'localtour'.  Una vez definido todo, ya se puede hacer uso del CRUD completo.

* En todas las operaciones GET con el nombre 'All{clase}, se puede realizar un filtrado donde se pueden indicar hasta 3 campos diferentes.

Campos de filtrado por clase:
	- teams: name / country / email
	ejemplo de url: ..?name=Visma
	- cyclists: name / specialty / birthplace
	ejemplo de url:  ..?specialty=escalador
	- stages: departure / arrival / type
	ejemplo de url:	..?arrival=Formigal
	- climbs: name / category / region
	ejemplo de url: ..?category=Hors Category
	- sponsors: name / country / email
	ejemplo de url: ..?email=movistar@movistar.com

Puntos desarrollados en 2ª evaluación:
- Tests unitarios y de integración
- Diseño de API Virtual
- Tests por cada caso de prueba en colección de Hoppscotch
- Instalación y funcionamiento de APIMan
- Publicación en Gateway y Developer Portal con necesidad de token para uso y 2 políticas de funcionamiento
- Uso de Git y GitHub
- Parametrización de colecciones 
- Uso de Docker compose para uso de entorno de pruebas con base de dados utilizando la api de forma local
