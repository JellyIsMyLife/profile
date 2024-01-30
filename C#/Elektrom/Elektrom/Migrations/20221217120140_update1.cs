using Microsoft.EntityFrameworkCore.Migrations;

#nullable disable

namespace Elektrom.Migrations
{
    public partial class update1 : Migration
    {
        protected override void Up(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.AddColumn<int>(
                name: "naudotojasId",
                table: "aspnetusers",
                type: "int(11)",
                nullable: true);
        }

        protected override void Down(MigrationBuilder migrationBuilder)
        {
            migrationBuilder.DropColumn(
                name: "naudotojasId",
                table: "aspnetusers");
        }
    }
}
