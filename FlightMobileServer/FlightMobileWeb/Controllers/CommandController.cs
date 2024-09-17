using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using FlightMobileWeb.Models;

namespace FlightMobileWeb.Controllers
{
    [Route("api/[controller]")]
    [ApiController]
    public class CommandController : ControllerBase
    {
        private readonly ITelnetClient telnetClient;

        public CommandController(ITelnetClient _telnetClient)
        {
            telnetClient = _telnetClient;
        }

        // POST: api/Default
        [HttpPost]
        async public Task<ActionResult> Post([FromBody] Command value)
        {
            //try to await result, if successful return code 200
            try
            {
                var res = await telnetClient.Execute(value);
                return Ok();
            }
            catch 
            {
                return this.BadRequest();
            }
        }
    }
}
