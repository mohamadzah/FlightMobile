using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace FlightMobileWeb.Models
{
    public interface ITelnetClient
    {
        //connect to the server.
        void Connect(string ip, int port);
        //write a message to the server.
        void Write(string command);
        //read back from the server.
        string Read();
        //disconnect from server.
        void Disconnect();
        //execution
        Task<Result> Execute(Command cmd);
    }
}
