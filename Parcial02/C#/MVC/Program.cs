using mvc;
using MySqlConnector;

var builder = WebApplication.CreateBuilder(args);
//Soporte para MySQL
builder.Services.AddMySqlDataSource(builder.Configuration.GetConnectionString("DataContext")!);
//Funcionalidad de MVC
builder.Services.AddControllersWithViews();
//Soporte para consultar datos
builder.Services.AddScoped<IDataContext, DataContext>();
//Construir
var app = builder.Build();

//Configurar pipeline de solicitudes HTTP
if (!app.Environment.IsDevelopment())
{
    //En caso de error en producción, oculta errores y manda página error
    app.UseExceptionHandler("/Home/Error");
    //Establece que app se ejecuta en https
    app.UseHsts();
}	
//Utiliza rutas para endpoints de controladores
app.UseRouting();
//Patrón de rutas
app.MapControllerRoute(
    name: "default",
    pattern: "{controller=Home}/{action=Index}/{id?}");
app.Run();