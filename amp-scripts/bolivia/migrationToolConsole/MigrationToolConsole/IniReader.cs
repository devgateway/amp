using System;
using System.Collections.Generic;
using System.Text;
using System.IO;
using System.Collections;
using System.Xml;

namespace MigrationToolConsole
{
    class IniReader
    {
        private XmlDocument xmlDocument = new XmlDocument();

        public void readfile(String fileName)
        {
            // get the configuration file
            FileInfo config_file = new FileInfo(fileName);
            if (config_file.Exists)
            {
                using (StreamReader stream_reader = new StreamReader(config_file.FullName))
                {
                    xmlDocument.LoadXml(stream_reader.ReadToEnd());
                    //string line;
                    //while ((line = stream_reader.ReadLine()) != null)

                    //{
                    //    SortedList sortedList = new SortedList();
                    //    String[] keyValuePair = new String[2];
                    //    //tomár el primer igual y partir
                    //    if(line.IndexOf('=') != -1){
                    //        keyValuePair[0] = line.Substring(0,line.IndexOf('=')); 
                    //        keyValuePair[1] = line.Substring(line.IndexOf('=')); 
                    //    }

                    //    sortedList.Add(keyValuePair[0], keyValuePair[1]);
                    //    String iniString = line;
                    //}
                }
            }
            else
            {
                throw new Exception("The INI file is missing.");
            }
        }

        internal string getSetting(string p)
        {
            if (xmlDocument != null)
            {
                XmlNodeList nl = xmlDocument.SelectNodes("//*[@key = '" + p + "']");
                if (nl.Count != 0)
                {
                    return nl[0].Attributes["value"].Value;
                }
                else
                {
                    nl = xmlDocument.SelectNodes("//*[@name = '" + p + "']");
                    if (nl.Count != 0) {
                        return nl[0].Attributes["connectionString"].Value;
                    }
                }
            }
            return "";
        }
    }
}
