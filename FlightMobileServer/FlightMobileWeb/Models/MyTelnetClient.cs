using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Concurrent;
using System.IO;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace FlightMobileWeb.Models
{
    //MyTelnetClient that implements Itelnetclient interface.
    class MyTelnetClient : ITelnetClient
    {
        private readonly BlockingCollection<AsyncCommand> _queue;

        private string Port { get; set; }
        private string Address { get; set; }

        private readonly TcpClient tcpClient;
        private NetworkStream stream;
        private bool checkConnection = false;

        public MyTelnetClient(IConfiguration configuration)
        {
            _queue = new BlockingCollection<AsyncCommand>();
            Address = configuration.GetValue<string>("TelnetClient:Address");
            Port = configuration.GetValue<string>("TelnetClient:Port");
            //Initialize the tcpClient.
            this.tcpClient = new TcpClient();
            Start();
        }

        public Task<Result> Execute(Command cmd)
        {
            var asyncCommand = new AsyncCommand(cmd);
            _queue.Add(asyncCommand);
            return asyncCommand.Task;
        }

        public void Start()
        {
            Task.Factory.StartNew(ProcessCommands);
        }

        public void TryEstablish()
        {
            //check if i have to check the values if valid.
            int port;
            try
            {
                port = int.Parse(Port);
                Connect(Address, port);
                stream = tcpClient.GetStream();
                //disable PROMPT 
                Write("data\n");
                //timeout 10 seconds if we don't get a response
                tcpClient.ReceiveTimeout = 10000;
                tcpClient.SendTimeout = 10000;
                checkConnection = true;
            }

            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

        public void ProcessCommands()
        {
            TryEstablish();
            if (checkConnection) {  //check whether we have connected succesfuly to tcp 
                foreach (AsyncCommand command in _queue.GetConsumingEnumerable()) {
                    Result res = Result.Ok;               
                    try {
                        if (checkConnection) {
                            double test;
                            string readMsg;
                            if ((command.Command.Aileron >= -1) && (command.Command.Aileron <= 1)) {
                                this.Write(command.Command.ChooseMessage("aileron", "set"));
                                this.Write(command.Command.ChooseMessage("aileron", "get"));
                                readMsg = Read();
                                test = TryParse(readMsg);
                                if (test != command.Command.Aileron)
                                {
                                    Console.WriteLine("FAIL! AILERON");
                                    res = Result.Invalid;
                                    continue;
                                }
                            }
                            if ((command.Command.Rudder >= -1) && (command.Command.Rudder <= 1)) {
                                this.Write(command.Command.ChooseMessage("rudder", "set"));
                                this.Write(command.Command.ChooseMessage("rudder", "get"));
                                readMsg = Read();
                                test = TryParse(readMsg);
                                if (test != command.Command.Rudder)
                                {
                                    Console.WriteLine("FAIL! RUDDER");
                                    res = Result.Invalid;
                                    continue;
                                }
                            }
                            if ((command.Command.Elevator >= -1) && (command.Command.Elevator <= 1)) {
                                this.Write(command.Command.ChooseMessage("elevator", "set"));
                                this.Write(command.Command.ChooseMessage("elevator", "get"));
                                readMsg = Read();
                                test = TryParse(readMsg);
                                if (test != command.Command.Elevator)
                                {
                                    Console.WriteLine("FAIL! ELEVATOR");
                                    res = Result.Invalid;
                                    continue;
                                }
                            }
                            if ((command.Command.Throttle >= 0) && (command.Command.Throttle <= 1)) {
                                this.Write(command.Command.ChooseMessage("throttle", "set"));
                                this.Write(command.Command.ChooseMessage("throttle", "get"));
                                readMsg = Read();
                                test = TryParse(readMsg);
                                if (test != command.Command.Throttle)
                                {
                                    res = Result.Invalid;
                                    continue;
                                }
                            }
                        }
                    }
                    catch  {
                        res = Result.NotOk; // if we cant even send values, that means we have server error (flightgear)
                    }
                    command.Completion.SetResult(res);
                }
            } 
            else
            {
                Console.WriteLine("Could not connect!");
            }
        }

        //try to parse the message read from the server, into a double 
        private double TryParse(string readMsg)
        {
            bool parse;
            try
            {
                parse = double.TryParse(readMsg, out double checkNumber);
                return checkNumber;
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
                return 1337; //an out of range value to represent a mismatch in values
            }
        }

        //Connecting to the server.
        public void Connect(string ip, int port)
        {
            //Try to establish a connection to the server.
            try
            {
                tcpClient.Connect(ip, port);
                checkConnection = true;
            }

            //Catch an exception in case establishing doesn't work.
            catch (IOException)
            {
                Console.WriteLine("Could not connect!");
                this.Disconnect();
            }
        }

        //Send a message to the server.
        public void Write(string command)
        {
            Byte[] encodedMsg = Encoding.ASCII.GetBytes(command);
            //try to send the message to the server.
            try
            {
                tcpClient.GetStream().Write(encodedMsg, 0, encodedMsg.Length);
            }
            catch
            {
                Console.WriteLine("Could not write to the server!");
            }
        }

        // NEED TO FIX THE EXCEPTION PROBLEMS !!!!!!!!!!!!!!!!!!!! .

        //Read back from the server.
        public string Read()
        {
            Byte[] sentBack = new Byte[1024];
            try
            {
                int len = tcpClient.GetStream().Read(sentBack, 0, sentBack.Length);
                string message = Encoding.ASCII.GetString(sentBack, 0, len);
                return message;
            }

            catch (IOException)
            {
                Console.WriteLine("The server didn't respond during time");
                Disconnect();
                checkConnection = false;
                return "";
            }

            catch (InvalidOperationException)
            {
                Console.WriteLine("Connection error suddenly occured!");
                Disconnect();
                checkConnection = false;
                return "";
            }
        }

        //Close the tcp client connection if established 
        public void Disconnect()
        {
            if (tcpClient != null)
            {
                tcpClient.Client.Close(); //disconnect from the server.
                stream.Close();
                checkConnection = false;
            }
            Console.WriteLine("Disconnected!");
        }
    }
}
