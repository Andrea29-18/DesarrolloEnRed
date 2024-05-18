using backendnet.Data.Seed;
using backendnet.Models;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;


using Microsoft.EntityFrameworkCore;

namespace backendnet.Data;

public class IndentityContext(DbContextOptions<IndentityContext> options) : IdentityDbContext<CustomIdentityUser>(options)
{

    public DbSet<Pelicula> Pelicula { get; set; }
    public DbSet<Categoria> Categorias{ get; set; }
   
   protected override void OnModelCreating(ModelBuilder modelBuilder){
        modelBuilder.ApplyConfiguration(new SeedCategoria());
        modelBuilder.ApplyConfiguration(new SeedPelicula());
        modelBuilder.SeedUserIdentityData();

        base.OnModelCreating(modelBuilder);
   }
}
