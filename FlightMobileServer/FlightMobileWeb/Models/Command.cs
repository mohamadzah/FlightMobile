using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Text.Json.Serialization;
using Newtonsoft.Json;


namespace FlightMobileWeb.Models
{
    public class Command
    {
        [JsonConstructor]
        public Command(double aileron, double rudder, double elevator, double throttle)
        {
            Aileron = aileron;
            Rudder = rudder;
            Elevator = elevator;
            Throttle = throttle;
        }

        public Command() { }

        [JsonProperty("aileron")]
        [JsonPropertyName("aileron")]
        public double Aileron { get; set; }

        [JsonProperty("rudder")]
        [JsonPropertyName("rudder")]
        public double Rudder { get; set; }

        [JsonProperty("elevator")]
        [JsonPropertyName("elevator")]
        public double Elevator { get; set; }

        [JsonProperty("throttle")]
        [JsonPropertyName("throttle")]
        public double Throttle { get; set; }

        //choose a message type that you want to send 
        public string ChooseMessage(string name, string type)
        {
            if (name == "aileron" && type == "set")
            {
                return "set /controls/flight/aileron " + Aileron + " \r\n";
            } 
            else if (name == "rudder" && type == "set")
            {
                return "set /controls/flight/rudder " + Rudder + " \r\n";
            }
            else if (name == "elevator" && type == "set")
            {
                return "set /controls/flight/elevator " + Elevator + " \r\n";
            }

            if (name == "aileron" && type == "get")
            {
                return "get /controls/flight/aileron \r\n";
            }
            else if (name == "rudder" && type == "get")
            {
                return "get /controls/flight/rudder \r\n";
            }
            else if (name == "elevator" && type == "get")
            {
                return "get /controls/flight/elevator \r\n";
            }

            if (name == "throttle" && type == "set")
            {
                return "set /controls/engines/current-engine/throttle " + Throttle + " \r\n";
            }
            return "get /controls/engines/current-engine/throttle \r\n";
        }
    }
}
