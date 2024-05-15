const jwt = require('jsonwebtoken');
const jwtSecret = process.env.JWT_SECRET;
const claimTypes = require('./config/claimtypes');
const GenerateToken = require('./services/jwttoken.service');

const Authorize = (rol) => {
  return async (req, res, next) => {
    try {
      const authHeader = req.header('Authorization');
      if (!authHeader.startsWith('Bearer ')) 
        return res.status(401).json();

      // Obtiene el token
      const token = authHeader.split(' ')[1];
      // Verifica el token; si no es válido envía error y salta al catch
      const decodedToken = jwt.verify(token, jwtSecret);

      // Verifica si el rol está autorizado
      if (rol.split(',').indexOf(decodedToken[claimTypes.Role]) == -1)
        return res.status(401).json();

      // Si tiene acceso, se permite continuar con el método y se obtienen lo datos del usuario
      req.decodedToken = decodedToken;

       // Código para enviar un nuevo token
       var minutosRestantes = ((decodedToken.exp - (new Date().getTime() / 1000)) / 60);
       if(minutosRestantes <= 5){ // mandamos un nuevo token
         var nuevoToken = GenerateToken(decodedToken[claimTypes.Name], decodedToken[claimTypes.GivenName], decodedToken[claimTypes.Role]);
         res.header('Set-Authorization', nuevoToken);
       }

       // Continua con el método
       next();
    } catch(error){
        return res.status(401).json();
    }
  }
}

module.exports = Authorize
