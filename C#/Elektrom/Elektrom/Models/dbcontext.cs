using System;
using System.Collections.Generic;
using Microsoft.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore.Metadata;

namespace Elektrom.Models
{
    public partial class dbcontext : DbContext
    {
        public dbcontext()
        {
        }

        public dbcontext(DbContextOptions<dbcontext> options)
            : base(options)
        {
        }

        public virtual DbSet<Adresai> Adresai { get; set; } = null!;
        public virtual DbSet<Atsiliepimai> Atsiliepimai { get; set; } = null!;
        public virtual DbSet<AtsiliepimuStatistikos> AtsiliepimuStatistikos { get; set; } = null!;
        public virtual DbSet<Kainos> Kainos { get; set; } = null!;
        public virtual DbSet<Kategorijos> Kategorijos { get; set; } = null!;
        public virtual DbSet<Krepseliai> Krepseliai { get; set; } = null!;
        public virtual DbSet<KrepselioPrekes> KrepselioPrekes { get; set; } = null!;
        public virtual DbSet<Naudotojai> Naudotojai { get; set; } = null!;
        public virtual DbSet<Nuotraukos> Nuotraukos { get; set; } = null!;
        public virtual DbSet<Prekes> Prekes { get; set; } = null!;
        public virtual DbSet<PrekesKategorijos> PrekesKategorijos { get; set; } = null!;
        public virtual DbSet<Uzsakymai> Uzsakymai { get; set; } = null!;
        public virtual DbSet<UzsakymuPrekes> UzsakymuPrekes { get; set; } = null!;

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            if (!optionsBuilder.IsConfigured)
            {
                optionsBuilder.UseMySql("server=127.0.0.1;database=elektrom;user=root;password=root", Microsoft.EntityFrameworkCore.ServerVersion.Parse("10.4.27-mariadb"));
            }
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {
            modelBuilder.UseCollation("utf8mb4_general_ci")
                .HasCharSet("utf8mb4");

            modelBuilder.Entity<Adresai>(entity =>
            {
                entity.HasKey(e => e.IdAdresas)
                    .HasName("PRIMARY");

                entity.ToTable("adresai");

                entity.Property(e => e.IdAdresas)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Adresas");

                entity.Property(e => e.AtnaujinimoData).HasColumnName("atnaujinimo_data");

                entity.Property(e => e.ButoNumeris)
                    .HasColumnType("int(11)")
                    .HasColumnName("buto_numeris");

                entity.Property(e => e.Gatve)
                    .HasMaxLength(255)
                    .HasColumnName("gatve");

                entity.Property(e => e.Miestas)
                    .HasMaxLength(255)
                    .HasColumnName("miestas");

                entity.Property(e => e.NamoNumeris)
                    .HasColumnType("int(11)")
                    .HasColumnName("namo_numeris");

                entity.Property(e => e.PastoKodas)
                    .HasMaxLength(255)
                    .HasColumnName("pasto_kodas");

                entity.Property(e => e.Salis)
                    .HasMaxLength(255)
                    .HasColumnName("salis");

                entity.Property(e => e.Savivaldybe)
                    .HasMaxLength(255)
                    .HasColumnName("savivaldybe");
            });

            modelBuilder.Entity<Atsiliepimai>(entity =>
            {
                entity.HasKey(e => e.IdAtsiliepimas)
                    .HasName("PRIMARY");

                entity.ToTable("atsiliepimai");

                entity.HasIndex(e => e.FkNaudotojasidNaudotojas, "raso");

                entity.HasIndex(e => e.FkPrekeidPreke, "turi_prekes");

                entity.Property(e => e.IdAtsiliepimas)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Atsiliepimas");

                entity.Property(e => e.AtnaujinimoLaikas).HasColumnName("atnaujinimo_laikas");

                entity.Property(e => e.FkNaudotojasidNaudotojas)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Naudotojasid_Naudotojas");

                entity.Property(e => e.FkPrekeidPreke)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Prekeid_Preke");

                entity.Property(e => e.Ivertinimas)
                    .HasColumnType("int(11)")
                    .HasColumnName("ivertinimas");

                entity.Property(e => e.Komentaras)
                    .HasMaxLength(255)
                    .HasColumnName("komentaras");

                entity.Property(e => e.SukurimoLaikas).HasColumnName("sukurimo_laikas");

                entity.HasOne(d => d.FkNaudotojasidNaudotojasNavigation)
                    .WithMany(p => p.Atsiliepimais)
                    .HasForeignKey(d => d.FkNaudotojasidNaudotojas)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("raso");

                entity.HasOne(d => d.FkPrekeidPrekeNavigation)
                    .WithMany(p => p.Atsiliepimais)
                    .HasForeignKey(d => d.FkPrekeidPreke)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("turi_prekes");
            });

            modelBuilder.Entity<AtsiliepimuStatistikos>(entity =>
            {
                entity.HasKey(e => e.IdAtsiliepimuStatistika)
                    .HasName("PRIMARY");

                entity.ToTable("atsiliepimu_statistikos");

                entity.Property(e => e.IdAtsiliepimuStatistika)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Atsiliepimu_statistika");

                entity.Property(e => e.AtnaujinimoData).HasColumnName("atnaujinimo_data");

                entity.Property(e => e.AtsiliepimoVidurkis).HasColumnName("atsiliepimo_vidurkis");

                entity.Property(e => e.Data).HasColumnName("data");

                entity.Property(e => e.MenesioDidziausiasVidurkis).HasColumnName("menesio_didziausias_vidurkis");

                entity.Property(e => e.MetuDidziausiasVidurkis).HasColumnName("metu_didziausias_vidurkis");

                entity.Property(e => e.SavaitesDidziausiasVidurkis).HasColumnName("savaites_didziausias_vidurkis");
            });

            modelBuilder.Entity<Kainos>(entity =>
            {
                entity.HasKey(e => e.IdKaina)
                    .HasName("PRIMARY");

                entity.ToTable("kainos");

                entity.HasIndex(e => e.FkPrekeidPreke, "turi_prekes1");

                entity.Property(e => e.IdKaina)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Kaina");

                entity.Property(e => e.FkPrekeidPreke)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Prekeid_Preke");

                entity.Property(e => e.Kaina).HasColumnName("kaina");

                entity.HasOne(d => d.FkPrekeidPrekeNavigation)
                    .WithMany(p => p.Kainos)
                    .HasForeignKey(d => d.FkPrekeidPreke)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("turi_prekes1");
            });

            modelBuilder.Entity<Kategorijos>(entity =>
            {
                entity.HasKey(e => e.IdKategorija)
                    .HasName("PRIMARY");

                entity.ToTable("kategorijos");

                entity.HasIndex(e => e.FkKategorijaidKategorija, "turi");

                entity.Property(e => e.IdKategorija)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Kategorija");

                entity.Property(e => e.Aprasymas)
                    .HasMaxLength(255)
                    .HasColumnName("aprasymas");

                entity.Property(e => e.AtnaujinimoData).HasColumnName("atnaujinimo_data");

                entity.Property(e => e.FkKategorijaidKategorija)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Kategorijaid_Kategorija");

                entity.Property(e => e.Pavadinimas)
                    .HasMaxLength(255)
                    .HasColumnName("pavadinimas");

                entity.HasOne(d => d.FkKategorijaidKategorijaNavigation)
                    .WithMany(p => p.InverseFkKategorijaidKategorijaNavigation)
                    .HasForeignKey(d => d.FkKategorijaidKategorija)
                    .HasConstraintName("turi");
            });

            modelBuilder.Entity<Krepseliai>(entity =>
            {
                entity.HasKey(e => e.IdKrepselis)
                    .HasName("PRIMARY");

                entity.ToTable("krepseliai");

                entity.HasIndex(e => e.FkNaudotojasidNaudotojas, "fk_Naudotojasid_Naudotojas")
                    .IsUnique();

                entity.Property(e => e.IdKrepselis)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Krepselis");

                entity.Property(e => e.FkNaudotojasidNaudotojas)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Naudotojasid_Naudotojas");

                entity.Property(e => e.PrekiuKiekis)
                    .HasColumnType("int(11)")
                    .HasColumnName("prekiu_kiekis");

                entity.Property(e => e.Suma).HasColumnName("suma");

                entity.HasOne(d => d.FkNaudotojasidNaudotojasNavigation)
                    .WithOne(p => p.Krepseliai)
                    .HasForeignKey<Krepseliai>(d => d.FkNaudotojasidNaudotojas)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("turi_naudotojus");
            });

            modelBuilder.Entity<KrepselioPrekes>(entity =>
            {
                entity.HasKey(e => e.IdKrepselioPreke)
                    .HasName("PRIMARY");

                entity.ToTable("krepselio_prekes");

                entity.HasIndex(e => e.FkPrekeidPreke, "priklauso");

                entity.HasIndex(e => e.FkKrepselisidKrepselis, "turi_krepseli");

                entity.Property(e => e.IdKrepselioPreke)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Krepselio_preke");

                entity.Property(e => e.AtnaujinimoData).HasColumnName("atnaujinimo_data");

                entity.Property(e => e.FkKrepselisidKrepselis)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Krepselisid_Krepselis");

                entity.Property(e => e.FkPrekeidPreke)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Prekeid_Preke");

                entity.Property(e => e.Kiekis)
                    .HasColumnType("int(11)")
                    .HasColumnName("kiekis");

                entity.HasOne(d => d.FkKrepselisidKrepselisNavigation)
                    .WithMany(p => p.KrepselioPrekes)
                    .HasForeignKey(d => d.FkKrepselisidKrepselis)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("turi_krepseli");

                entity.HasOne(d => d.FkPrekeidPrekeNavigation)
                    .WithMany(p => p.KrepselioPrekes)
                    .HasForeignKey(d => d.FkPrekeidPreke)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("priklauso");
            });

            modelBuilder.Entity<Naudotojai>(entity =>
            {
                entity.HasKey(e => e.IdNaudotojas)
                    .HasName("PRIMARY");

                entity.ToTable("naudotojai");

                entity.HasIndex(e => e.FkAdresasidAdresas, "turi_adresus");

                entity.Property(e => e.IdNaudotojas)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Naudotojas");

                entity.Property(e => e.FkAdresasidAdresas)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Adresasid_Adresas");

                entity.Property(e => e.PaskutinisPrisijungimas).HasColumnName("paskutinis_prisijungimas");

                entity.Property(e => e.Pastas)
                    .HasMaxLength(255)
                    .HasColumnName("pastas");

                entity.Property(e => e.Pavarde)
                    .HasMaxLength(255)
                    .HasColumnName("pavarde");

                entity.Property(e => e.RegistracijosData).HasColumnName("registracijos_data");

                entity.Property(e => e.Slaptazodis)
                    .HasMaxLength(255)
                    .HasColumnName("slaptazodis");

                entity.Property(e => e.TelNumeris)
                    .HasMaxLength(255)
                    .HasColumnName("tel_numeris");

                entity.Property(e => e.Tipas)
                    .HasMaxLength(255)
                    .HasColumnName("tipas");

                entity.Property(e => e.Vardas)
                    .HasMaxLength(255)
                    .HasColumnName("vardas");

                entity.HasOne(d => d.FkAdresasidAdresasNavigation)
                    .WithMany(p => p.Naudotojais)
                    .HasForeignKey(d => d.FkAdresasidAdresas)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("turi_adresus");
            });

            modelBuilder.Entity<Nuotraukos>(entity =>
            {
                entity.HasKey(e => e.IdNuotrauka)
                    .HasName("PRIMARY");

                entity.ToTable("nuotraukos");

                entity.HasIndex(e => e.FkPrekeidPreke, "turi_prekes2");

                entity.Property(e => e.IdNuotrauka)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Nuotrauka");

                entity.Property(e => e.Aukstis)
                    .HasColumnType("int(11)")
                    .HasColumnName("aukstis");

                entity.Property(e => e.Dydis)
                    .HasPrecision(20)
                    .HasColumnName("dydis");

                entity.Property(e => e.FkPrekeidPreke)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Prekeid_Preke");

                entity.Property(e => e.Formatas)
                    .HasMaxLength(255)
                    .HasColumnName("formatas");

                entity.Property(e => e.Pavadinimas)
                    .HasMaxLength(255)
                    .HasColumnName("pavadinimas");

                entity.Property(e => e.Plotis)
                    .HasColumnType("int(11)")
                    .HasColumnName("plotis");

                entity.HasOne(d => d.FkPrekeidPrekeNavigation)
                    .WithMany(p => p.Nuotraukos)
                    .HasForeignKey(d => d.FkPrekeidPreke)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("turi_prekes2");
            });

            modelBuilder.Entity<Prekes>(entity =>
            {
                entity.HasKey(e => e.IdPreke)
                    .HasName("PRIMARY");

                entity.ToTable("prekes");

                entity.HasIndex(e => e.FkAtsiliepimuStatistikaidAtsiliepimuStatistika, "fk_Atsiliepimu_statistikaid_Atsiliepimu_statistika")
                    .IsUnique();

                entity.Property(e => e.IdPreke)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Preke");

                entity.Property(e => e.Aprasymas)
                    .HasMaxLength(255)
                    .HasColumnName("aprasymas");

                entity.Property(e => e.AtnaujinimoData).HasColumnName("atnaujinimo_data");

                entity.Property(e => e.FkAtsiliepimuStatistikaidAtsiliepimuStatistika)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Atsiliepimu_statistikaid_Atsiliepimu_statistika");

                entity.Property(e => e.Kiekis)
                    .HasColumnType("int(11)")
                    .HasColumnName("kiekis");

                entity.Property(e => e.Pavadinimas)
                    .HasMaxLength(255)
                    .HasColumnName("pavadinimas");

                entity.Property(e => e.Rodymas).HasColumnName("rodymas");

                entity.HasOne(d => d.FkAtsiliepimuStatistikaidAtsiliepimuStatistikaNavigation)
                    .WithOne(p => p.Preke)
                    .HasForeignKey<Prekes>(d => d.FkAtsiliepimuStatistikaidAtsiliepimuStatistika)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("turi_atsiliepimu_satistika");
            });

            modelBuilder.Entity<PrekesKategorijos>(entity =>
            {
                entity.HasKey(e => new { e.FkKategorijaidKategorija, e.FkPrekeidPreke })
                    .HasName("PRIMARY")
                    .HasAnnotation("MySql:IndexPrefixLength", new[] { 0, 0 });

                entity.ToTable("prekes_kategorijos");

                entity.Property(e => e.FkKategorijaidKategorija)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Kategorijaid_Kategorija");

                entity.Property(e => e.FkPrekeidPreke)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Prekeid_Preke");

                entity.HasOne(d => d.FkKategorijaidKategorijaNavigation)
                    .WithMany(p => p.PrekesKategorijos)
                    .HasForeignKey(d => d.FkKategorijaidKategorija)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("turi_kategorijas");
            });

            modelBuilder.Entity<Uzsakymai>(entity =>
            {
                entity.HasKey(e => e.IdUzsakymas)
                    .HasName("PRIMARY");

                entity.ToTable("uzsakymai");

                entity.HasIndex(e => e.FkNaudotojasidNaudotojas, "sukuria");

                entity.Property(e => e.IdUzsakymas)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Uzsakymas");

                entity.Property(e => e.Data).HasColumnName("data");

                entity.Property(e => e.FkNaudotojasidNaudotojas)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Naudotojasid_Naudotojas");

                entity.Property(e => e.PrekiuKiekis)
                    .HasColumnType("int(11)")
                    .HasColumnName("prekiu_kiekis");

                entity.Property(e => e.Pristatymas)
                    .HasMaxLength(50)
                    .HasColumnName("pristatymas");

                entity.Property(e => e.Statusas)
                    .HasMaxLength(255)
                    .HasColumnName("statusas");

                entity.Property(e => e.Suma).HasColumnName("suma");

                entity.HasOne(d => d.FkNaudotojasidNaudotojasNavigation)
                    .WithMany(p => p.Uzsakymais)
                    .HasForeignKey(d => d.FkNaudotojasidNaudotojas)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("sukuria");
            });

            modelBuilder.Entity<UzsakymuPrekes>(entity =>
            {
                entity.HasKey(e => e.IdUzsakymoPrekes)
                    .HasName("PRIMARY");

                entity.ToTable("uzsakymu_prekes");

                entity.HasIndex(e => e.FkPrekeidPreke, "priklauso_uzsakymui");

                entity.HasIndex(e => e.FkUzsakymasidUzsakymas, "turi_uzsakymus");

                entity.Property(e => e.IdUzsakymoPrekes)
                    .HasColumnType("int(11)")
                    .HasColumnName("id_Uzsakymo_prekes");

                entity.Property(e => e.AtnaujinimoData).HasColumnName("atnaujinimo_data");

                entity.Property(e => e.FkPrekeidPreke)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Prekeid_Preke");

                entity.Property(e => e.FkUzsakymasidUzsakymas)
                    .HasColumnType("int(11)")
                    .HasColumnName("fk_Uzsakymasid_Uzsakymas");

                entity.Property(e => e.Kiekis)
                    .HasColumnType("int(11)")
                    .HasColumnName("kiekis");

                entity.HasOne(d => d.FkPrekeidPrekeNavigation)
                    .WithMany(p => p.UzsakymuPrekes)
                    .HasForeignKey(d => d.FkPrekeidPreke)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("priklauso_uzsakymui");

                entity.HasOne(d => d.FkUzsakymasidUzsakymasNavigation)
                    .WithMany(p => p.UzsakymuPrekes)
                    .HasForeignKey(d => d.FkUzsakymasidUzsakymas)
                    .OnDelete(DeleteBehavior.ClientSetNull)
                    .HasConstraintName("turi_uzsakymus");
            });

            OnModelCreatingPartial(modelBuilder);
        }

        partial void OnModelCreatingPartial(ModelBuilder modelBuilder);
    }
}
