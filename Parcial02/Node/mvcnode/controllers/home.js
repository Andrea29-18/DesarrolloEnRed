const alumno = require('../models/alumno');

const index = async function(req, res, next){
    try {
        let items = await alumno.list()
        res.render("home", {
            model: items
        });
    } catch (error) {
        next(error);
    }
};

module.exports = { index };