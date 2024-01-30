using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Threading.Tasks;
using Elektrom.Models;
using Microsoft.AspNetCore.Identity;

namespace Elektrom.Areas.Identity.Data;

// Add profile data for application users by adding properties to the AuthUser class
public class AuthUser : IdentityUser
{
    [PersonalData]
    [Column(TypeName = "int(11)")]
    public int? naudotojasId { get; set; }
}

