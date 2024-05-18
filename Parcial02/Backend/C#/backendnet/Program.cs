using Microsoft.EntityFrameworkCore;
using backendnet.Data;
using backendnet.Models;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Authentication.JwtBearer;
using Microsoft.IdentityModel.Tokens;
using System.Text;
using backendnet.Middlewares;
using backendnet.Services;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddScoped<JwtTokenService>();

//Agregar el soporte para MySQL
var connectionString = builder.Configuration.GetConnectionString("DataContext");
builder.Services.AddDbContext<IndentityContext>(options =>{
    options.UseMySql(connectionString, ServerVersion.AutoDetect(connectionString));
});

builder.Services.AddIdentity<CustomIdentityUser, IdentityRole>(options => 
{
    options.User.RequireUniqueEmail = true;
    //Cambie aqui como quiere se maneje sus contraseñas
    options.Password.RequireDigit = false;
    options.Password.RequireLowercase = false;
    options.Password.RequireNonAlphanumeric = false;
    options.Password.RequireUppercase = false;
    options.Password.RequiredLength = 6;
    options.Password.RequiredUniqueChars = 1;
})
.AddEntityFrameworkStores<IndentityContext>();

//Soporte para JWT
builder.Services
    .AddHttpContextAccessor() //Para acceder al HttpContext()
    .AddAuthorization() // Para autorizar en cada método el acceso
    .AddAuthentication(options => //Para autenticar con JWT
    {
        options.DefaultAuthenticateScheme = JwtBearerDefaults.AuthenticationScheme;
        options.DefaultChallengeScheme = JwtBearerDefaults.AuthenticationScheme;
    })
    .AddJwtBearer(options =>
    {
        options.TokenValidationParameters = new TokenValidationParameters
        {
            ValidateIssuer = true,
            ValidateAudience = true,
            ValidateLifetime = true,
            ValidateIssuerSigningKey = true,
            ValidIssuer = builder.Configuration["Jwt:Issuer"], //Leido desde appSettings
            ValidAudience = builder.Configuration["JWT:Audience"], //Leido desde appSetting
            IssuerSigningKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(builder.Configuration["Jwt:Secret"]!))
        };
    });

builder.Services.AddCors(options =>{
    options.AddDefaultPolicy(
        policy =>
        {
            policy.WithOrigins("http://localhost:3000", "http://localhost:8080")
                              .AllowAnyHeader()
                              .WithMethods("GET", "POST", "PUT", "DELETE");
        }
    );
});

builder.Services.AddControllers();

builder.Services.AddSwaggerGen();

var app = builder.Build();

if(app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseExceptionHandler("/error");

app.UseRouting();

app.UseAuthentication();

app.UseAuthorization();

app.UseSlidingExpirationJwt();

app.UseCors();

app.MapControllers();

app.Run();
