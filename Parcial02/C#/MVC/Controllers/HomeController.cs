using Microsoft.AspNetCore.Mvc;
namespace mvc;

public class HomeController : Controller
{
    private readonly IDataContext _context;
    public HomeController(IDataContext context)
    {
        _context = context;
    }
    
    public async Task<IActionResult> Index()
    {
        //Obtiene lista productos
        List<Producto> productos = await _context.ObtenerProductosAsync();
        //Envia modelo a vista
        return View(productos);
    }
    public IActionResult Error()
    {
        return View();
    }
}