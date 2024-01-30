using System.Web.Mvc;
using System.Web.Optimization;
using System.Web.Routing;

namespace LD2_Filmai
{
    public class MvcApplication : System.Web.HttpApplication
    {
        protected void Application_Start()
        {
            AreaRegistration.RegisterAllAreas();
            FilterConfig.RegisterGlobalFilters(GlobalFilters.Filters);
            RouteConfig.RegisterRoutes(RouteTable.Routes);
            BundleConfig.RegisterBundles(BundleTable.Bundles);
        }
    }

    public static class Counters
    {
        public static int aktoriusCounter = 0;
        public static int filmasCounter = 0;
        public static int garsoTakelisCounter = 0;
        public static int reitingasCounter = 0;
        public static int mokejimasCounter = 0;
    }
}