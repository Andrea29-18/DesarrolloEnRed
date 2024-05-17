const jwt = require('jsonwebtoken')
const jwtSecret = process.env.JWT_SECRET
const claimTypes = require('../config/claimtypes')
const GenerateToken = require('../services/jwttoken.service')

const Authorize = (rol) => {
  return async (req, res, next) => {
    try {
      const authHeader = req.header('Authorization')
      if (!authHeader.startsWith('Bearer ')) 
        return res.status(401).json()


      const token = authHeader.split(' ')[1]

      const decodedToken = jwt.verify(token, jwtSecret)


      if (rol.split(',').indexOf(decodedToken[claimTypes.Role]) == -1)
        return res.status(401).json()


      req.decodedToken = decodedToken


      var minutosRestantes = (decodedToken.exp - (new Date().getTime() / 1000)) / 60
      if(minutosRestantes < 5){ 
        var nuevoToken = GeneraToken(decodedToken[claimTypes.Name], decodedToken[claimTypes.GivenName], decodedToken[claimTypes.Role])
        res.header("Set-Authorization", nuevoToken)
      }

       // Continua con el método
       next()
    } catch(error){
        return res.status(401).json();
    }
  }
}

module.exports = Authorize
