using System;
using System.Collections.Generic;
using System.Linq;
using System.IO;
using System.Net;
using System.Threading.Tasks;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Configuration;
using System.Net.Http;
using System.Net.Http.Headers;

namespace FlightMobileWeb.Controllers
{
    [Route("[controller]")]
    [ApiController]
    public class ScreenshotController : ControllerBase
    {
        private string Port { get; set; }
        private string Address { get; set; }

        public ScreenshotController(IConfiguration configuration)
        {
            Address = configuration.GetValue<string>("Screenshot:Address");
            Port = configuration.GetValue<string>("Screenshot:Port");
        }

        // GET: api/Screenshot
        [HttpGet]
        public async Task<IActionResult> Get()
        {
            //create the http address to request a screenshot on.
            string requestScreenshot = "http://" + Address + ":" + Port + "/screenshot";
            //create httpclient and set a timeout of 10 seconds.
            var httpClient = new HttpClient { Timeout = TimeSpan.FromMilliseconds(10000) };
            try
            {
                //try to get response
                var response = await httpClient.GetAsync(requestScreenshot);
                var screenshot = (dynamic)null;
                if (response.IsSuccessStatusCode)
                {
                    screenshot = await httpClient.GetByteArrayAsync(requestScreenshot);
                }
                return File(screenshot, "image/jpg");
            }
            catch 
            {
                return BadRequest();
            }
        }

    }
}
