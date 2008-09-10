using System;
using System.Collections.Generic;
using System.Text;
using System.Data.Common;
using System.Data;
using System.IO;

namespace MigrationToolConsole
{
    class Program
    {
        static String sourceConnectionString = "";
        static String destinationConnectionString = "";
        static String sourceConnectionProvider = "";
        static String destinationConnectionProvider = "";
        static Boolean scriptsOnly = false;
        static String tablesCsv = "";
        static String tablesMode = "";
        static String[] tablesArray;
        static String scriptsCsv = "";
        static String[] scriptsArray;
        static Boolean isDeleteData = false;
        static String logFile = "";

        static StreamWriter logWriter;

        static void Main(string[] args)
        {

            if (args.Length == 0)
            {
                Console.WriteLine("Parameter not specified.\n\rUsage: MigrationToolConsole.exe file.config");
                Console.Read();
                return;
            
            }

            Console.WriteLine("Reading settings from " + args[0] + ".");
            try
            {
                GetSettings(args[0]);
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: " + ex.Message);
                Console.Read();
                return;
            }
            
            if(logFile != "")
                logWriter = new StreamWriter(logFile + DateTime.Now.ToString("MMddyyyyHHmm") + ".log", false);

            if (logWriter != null)
            {
                logWriter.AutoFlush = true;
            }
            if (!scriptsOnly)
            {
                Console.WriteLine("Getting list of tables to be copied.");
                if (logWriter != null)
                {
                    logWriter.WriteLine("Getting list of tables to be copied.");
                }
            }
            DbProviderFactory factory = DbProviderFactories.GetFactory(destinationConnectionProvider);

            DbConnection connection = factory.CreateConnection();
            connection.ConnectionString = destinationConnectionString;
            try
            {
                connection.Open();
            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: " + ex.Message);
                if (logWriter != null)
                {
                    logWriter.WriteLine("Exception: " + ex.Message);
                }
                return;
            }

            DataRowCollection drCol = null;
            if (!scriptsOnly)
            {
                try
                {
                    drCol = GetDestinationTables();
                }
                catch (Exception ex)
                {
                    Console.WriteLine("Exception: " + ex.Message);
                    if (logWriter != null)
                    {
                        logWriter.WriteLine("Exception: " + ex.Message);
                    }
                    return;
                }
            }



            DbTransaction transaction = connection.BeginTransaction();

            if (!scriptsOnly)
            {

                if (isDeleteData)
                {
                    Console.WriteLine("Deleting data from destination tables.");

                    if (logWriter != null)
                    {
                        logWriter.WriteLine("Deleting data from destination tables.");
                    }
                    try
                    {
                        DeleteTableData(drCol, connection, factory);

                    }
                    catch (Exception ex)
                    {
                        Console.WriteLine("Error truncating data. Exception: " + ex.Message);
                        if (logWriter != null)
                        {
                            logWriter.WriteLine("Error truncating data. Exception: " + ex.Message);
                        }
                    }

                }
            }



            if (!scriptsOnly)
            {
                foreach (DataRow dr in drCol)
                {
                    if (IsIncluded(dr["TABLE_NAME"].ToString()))
                    {
                        Console.WriteLine("Copying data for table " + dr["TABLE_NAME"].ToString() + ".");
                        if (logWriter != null)
                        {
                            logWriter.WriteLine("Copying data for table " + dr["TABLE_NAME"].ToString() + ".");
                        }
                        try
                        {
                            CopyData(dr["TABLE_NAME"].ToString(), connection, factory);
                        }
                        catch (Exception ex)
                        {
                            Console.WriteLine("Exception occurred when copying table " + dr["TABLE_NAME"].ToString() + ". Error: " + ex.Message);
                            Console.WriteLine("Rolling back transaction.");
                            if (logWriter != null)
                            {
                                logWriter.WriteLine("Exception occurred when copying table " + dr["TABLE_NAME"].ToString() + ". Error: " + ex.Message);
                                logWriter.WriteLine("Rolling back transaction.");
                            }
                            transaction.Rollback();
                            return;
                        }
                    }
                }
            }

            Console.WriteLine("Executing SQL Scripts.");
            if (scriptsArray != null)
            {
                foreach (String fileName in scriptsArray)
                {
                    Console.WriteLine("Executing " + fileName + ".");
                    try
                    {
                        using (StreamReader sr = new StreamReader(fileName))
                        {

                            DbCommand command = factory.CreateCommand();
                            command.CommandText = sr.ReadToEnd();
                            command.CommandType = CommandType.Text;
                            command.Connection = connection;
                            command.CommandTimeout = 0;

                            command.ExecuteNonQuery();
                        }
                    }
                    catch (Exception e)
                    {
                        // Let the user know what went wrong.
                        Console.WriteLine("The file could not be read:");
                        Console.WriteLine(e.Message);
                        transaction.Rollback();
                        return;
                    }

                }
            }
            transaction.Commit();
            Console.WriteLine("Process finished.");
        }

        private static void CopyData(string sTable, DbConnection connection, DbProviderFactory factory)
        {
            try
            {
                DbCommandBuilder builder = factory.CreateCommandBuilder();
                String separator = builder.QuoteSuffix;

                String queryString =
                    "SELECT * from " + separator + sTable + separator;


                DbCommand command = factory.CreateCommand();
                command.CommandText = queryString;
                command.Connection = connection;

                DbDataAdapter adapter = factory.CreateDataAdapter();
                adapter.SelectCommand = command;
                builder.DataAdapter = adapter;


                adapter.InsertCommand = builder.GetInsertCommand();

                DataTable table = new DataTable();
                adapter.Fill(table);

                DbProviderFactory factorySource = DbProviderFactories.GetFactory(sourceConnectionProvider);

                DbConnection connectionSource = factorySource.CreateConnection();
                connectionSource.ConnectionString = sourceConnectionString;

                DbDataAdapter da1 = factorySource.CreateDataAdapter();
                DbCommandBuilder builderSource = factorySource.CreateCommandBuilder();
                builderSource.DataAdapter = da1;
                String separatorSourcePrefix = builderSource.QuotePrefix;
                String separatorSourceSuffix = builderSource.QuoteSuffix;
                if (separatorSourcePrefix == "")
                {
                    separatorSourcePrefix = "[";
                    separatorSourceSuffix = "]";
                }

                String querySource =
                    "SELECT * from " + separatorSourcePrefix + sTable + separatorSourceSuffix;

                DbCommand commandSource = factorySource.CreateCommand();
                commandSource.CommandText = querySource;
                commandSource.Connection = connectionSource;
                da1.SelectCommand = commandSource;

                DataSet ds1 = new DataSet();
                da1.Fill(ds1);
                DataTable srcTable = ds1.Tables[0];
                foreach (DataRow dr in srcTable.Rows)
                {
                    DataRow newRow = table.NewRow();
                    newRow.ItemArray = dr.ItemArray;
                    table.Rows.Add(newRow);
                }

                adapter.Update(table);
            }
            catch (Exception ex)
            {
                throw ex;
            }


        }

        private static void DeleteTableData(DataRowCollection drCol, DbConnection connection, DbProviderFactory factory)
        {

            DbCommandBuilder builder = factory.CreateCommandBuilder();
            String separator = builder.QuoteSuffix;

            DbCommand cmd = connection.CreateCommand();
            cmd.CommandTimeout = 0;
            if (drCol != null && drCol.Count > 0)
            {
                foreach (DataRow dr in drCol)
                {
                    if (IsIncluded(dr["TABLE_NAME"].ToString())) { 
                        try
                        {
                            cmd.CommandText = "DELETE FROM " + separator + dr["TABLE_NAME"].ToString() + separator;
                            cmd.ExecuteNonQuery();
                        }
                        catch (Exception ex)
                        {
                            throw ex;
                        }
                    }
                }
            }

        }

        private static bool IsIncluded(String p)
        {
            if (tablesArray != null)
            {
                List<String> myList = new List<String>(tablesArray);
                if (tablesMode == "include")
                {
                    if (myList.Contains(p))
                        return true;
                }
                else if (tablesMode == "exclude")
                {
                    if (!myList.Contains(p))
                        return true;
                }
            }
            else
                return true;
            
            return false;
        }

        private static void GetSettings(String iniFileName)
        {
            IniReader iniReader = new IniReader();
            iniReader.readfile(iniFileName);
            sourceConnectionString = iniReader.getSetting("SourceConn");
            destinationConnectionString = iniReader.getSetting("DestinationConn");

            sourceConnectionProvider = iniReader.getSetting("SourceConnProvider");
            destinationConnectionProvider = iniReader.getSetting("DestConnProvider");

            tablesMode = iniReader.getSetting("TablesMode");
            tablesCsv = iniReader.getSetting("Tables");
            if(tablesCsv != "")
                tablesArray = tablesCsv.Split(',');

            scriptsCsv = iniReader.getSetting("Scripts");
            if (scriptsCsv != "")
                scriptsArray = scriptsCsv.Split(',');
            scriptsOnly = Boolean.Parse(iniReader.getSetting("ScriptsOnly"));
            isDeleteData = Boolean.Parse(iniReader.getSetting("DeleteTableData"));
            logFile = iniReader.getSetting("LogFile");

            if (sourceConnectionProvider == null || sourceConnectionString == null ||
                destinationConnectionProvider == null || destinationConnectionString == null ||
                logFile == null || scriptsCsv == null)
            {
                throw new Exception("Parameters missing.");
            }
        }

        private static DataRowCollection GetDestinationTables()
        {
            try 
            { 
                DbProviderFactory factory = DbProviderFactories.GetFactory(destinationConnectionProvider);

                DbConnection connection = factory.CreateConnection();
                connection.ConnectionString = destinationConnectionString;
                connection.Open();
                DataTable dataTable = connection.GetSchema("Tables");
                connection.Close();
                return dataTable.Rows;
            }
            catch (Exception ex)
            {
                throw ex;
            }
        }
    }
}
