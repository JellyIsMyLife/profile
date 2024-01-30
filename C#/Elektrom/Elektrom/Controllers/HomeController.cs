using System.Diagnostics;
using Microsoft.AspNetCore.Mvc;
using Elektrom.Models;
using Microsoft.EntityFrameworkCore;

namespace Elektrom.Controllers;

public class HomeController : Controller
{
    private readonly ILogger<HomeController> _logger;
    private readonly dbcontext _db;

    public HomeController(ILogger<HomeController> logger, dbcontext db)
    {
        _logger = logger;
        _db = db;
    }

    public IActionResult Index()
    {
        return View();
    }

    public IActionResult Privacy()
    {
        return View();
    }

    public List<AtsiliepimuStatistikos> Atsiliepimai()
    {
        return _db.AtsiliepimuStatistikos
            .Include(x => x.Preke)
            .ToList();
    }

    [ResponseCache(Duration = 0, Location = ResponseCacheLocation.None, NoStore = true)]
    public IActionResult Error()
    {
        return View(new ErrorViewModel { RequestId = Activity.Current?.Id ?? HttpContext.TraceIdentifier });
    }
}