//POR CADA CAMBIO SE DEBE DE ACTUALIZAR "node swagger.js" CON SU ACTUALIZACION

const swaggerAutogen = require('swagger-autogen')();

const doc = {
    info: {
        title: 'Backend Node.js API',
        description: 'Esta es una API en node.js'
    },
    host: 'localhost:3000'
};

const outputfile = './swagger-output.json';
const routes = ['./index.js'];

swaggerAutogen(outputfile,routes,doc);