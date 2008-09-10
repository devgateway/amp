#region using section
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.IO;
using System.Data.Common;

using System.Data.OleDb;
using System.Data.SqlClient;
using MySql.Data.MySqlClient;

#endregion

namespace DBImporter
{
    public partial class frmRefresh : Form
    {
        DataSet ds;
        string strSourceConn;
        string strDestConn;
        string strDeleteLogFile;
        string strLoadLogFile;
        delegate void lblStatusdelegate(string pTableName);

        Boolean deleteData = false;
        Boolean isAutomated = false;
        String tableCsv;
        String scriptCsv;

        public frmRefresh()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            txtSource.Text = System.Configuration.ConfigurationManager.ConnectionStrings["SourceConn"].ConnectionString;
            txtDestination.Text = System.Configuration.ConfigurationManager.ConnectionStrings["DestinationConn"].ConnectionString;
            tableCsv = System.Configuration.ConfigurationManager.AppSettings["tables"];
            scriptCsv = System.Configuration.ConfigurationManager.AppSettings["scripts"];
            deleteData = Boolean.Parse(System.Configuration.ConfigurationManager.AppSettings["DeleteTableData"]);
            isAutomated = Boolean.Parse(System.Configuration.ConfigurationManager.AppSettings["IsAutomated"]);
        }

        private void btnDelete_Click(object sender, EventArgs e)
        {
            btnDelete.Enabled = false;
            DeleteTableData();
            MessageBox.Show("Data Deleted Successfully");
            btnDelete.Enabled = true;
        }

        private void btnLoad_Click(object sender, EventArgs e)
        {
            btnLoad.Enabled = false;
            btnCancel.Enabled = true;
            pBar1.Maximum = lstInclude.Items.Count;
            bgWorker1.RunWorkerAsync();
        }        

        private void btnCancel_Click(object sender, EventArgs e)
        {
            this.bgWorker1.CancelAsync();
            btnCancel.Enabled = false;            
        }

        private void SetStatusLabel(string pTableName)
        {
            lblStatus.Text = pTableName;
        }

        private void DeleteTableData()
        {
            using (MySqlConnection conn = new MySqlConnection(strDestConn))
            {
                conn.Open();
                MySqlCommand cmd = conn.CreateCommand();
                cmd.CommandTimeout = 0;
                if (ds != null && ds.Tables[0].Rows.Count > 0)
                {
                    foreach (DataRow dr in ds.Tables[0].Rows)
                    {
                        cmd.CommandText = "ALTER TABLE  `" + dr[0].ToString() + "` DISABLE KEYS ";
                        cmd.ExecuteNonQuery();
                    }

                    strDeleteLogFile = "DeleteLog" + DateTime.Now.ToString("MMddyyyyHHmm") + ".txt";
                    using (StreamWriter sw = new StreamWriter(strDeleteLogFile, false))
                    {
                        sw.AutoFlush = true;
                        foreach (object dr in lstInclude.Items)
                        {
                            try
                            {

                                cmd.CommandText = "TRUNCATE TABLE " + dr.ToString();
                                cmd.ExecuteNonQuery();
                            }
                            catch (Exception ex)
                            {
                                sw.WriteLine("Error while deleting data in table " + dr.ToString() + ".Error is " + ex.Message);
                                sw.WriteLine();
                            }
                        }
                    }
                    foreach (DataRow dr in ds.Tables[0].Rows)
                    {
                        cmd.CommandText = "ALTER TABLE `" + dr[0].ToString() + "` ENABLE KEYS";
                        cmd.ExecuteNonQuery();
                    }
                }                
            }
        }

        private void CopyData(string sourceConnStr,string destConnStr, string pTable)
        {
            try
            {
                DbProviderFactory factory = DbProviderFactories.GetFactory("MySql.Data.MySqlClient");

                DbConnection connection = factory.CreateConnection();
                connection.ConnectionString = destConnStr;

                using (connection)
                {
                    string queryString =
                        "SELECT * from " + pTable;

                    DbCommand command = factory.CreateCommand();
                    command.CommandText = queryString;
                    command.Connection = connection;

                    DbDataAdapter adapter = factory.CreateDataAdapter();
                    adapter.SelectCommand = command;

                    DbCommandBuilder builder = factory.CreateCommandBuilder();
                    builder.DataAdapter = adapter;

                    adapter.InsertCommand = builder.GetInsertCommand();
                    adapter.UpdateCommand = builder.GetUpdateCommand();
                    adapter.DeleteCommand = builder.GetDeleteCommand();

                    DataTable table = new DataTable();
                    adapter.Fill(table);
                    connection.Open();

                    using (OleDbConnection srcConn = new OleDbConnection(sourceConnStr))
                    {
                        OleDbDataAdapter da1 = new OleDbDataAdapter("SELECT * FROM " + pTable, srcConn);
                        DataSet ds1 = new DataSet();
                        da1.Fill(ds1);
                        DataTable srcTable = ds1.Tables[0];
                        foreach (DataRow dr in srcTable.Rows)
                        {
                            DataRow newRow = table.NewRow();
                            newRow.ItemArray = dr.ItemArray;
                            table.Rows.Add(newRow);
                        }
                    }

                    adapter.Update(table);
                }
            }
            catch (Exception ex)
            {
            }


        }

        
        #region BackGround Worker Functions
        private void bgWorker1_DoWork(object sender, DoWorkEventArgs e)
        {
            lblStatusdelegate setStatusLabel = new lblStatusdelegate(SetStatusLabel);
            strLoadLogFile = "LoadLog" + DateTime.Now.ToString("MMddyyyyHHmm") + ".txt";
            using (StreamWriter sw = new StreamWriter(strLoadLogFile, false))
            {
                sw.AutoFlush = true;
                foreach (object dr in lstInclude.Items)
                {
                    if (bgWorker1.CancellationPending)
                        e.Cancel = true;
                    else
                    {
                        try
                        {
                            this.Invoke(setStatusLabel, new object[] { dr.ToString() });
                            CopyData(strSourceConn, strDestConn, dr.ToString());
                            sw.WriteLine(dr.ToString() + " Loaded successfully at " + DateTime.Now.ToString());
                            sw.WriteLine();
                        }
                        catch (Exception ex)
                        {
                            sw.WriteLine("Error While Loading Data to Table " + dr.ToString() + " Error is " + ex.Message);
                            sw.WriteLine();
                        }
                        finally
                        {
                            bgWorker1.ReportProgress(0, dr.ToString());
                        }
                    }
                }
            }
        }

        private void bgWorker1_ProgressChanged(object sender, ProgressChangedEventArgs e)
        {
            string strTable = (string)e.UserState;
            pBar1.Value++;
        }

        private void bgWorker1_RunWorkerCompleted(object sender, RunWorkerCompletedEventArgs e)
        {
            if (e.Cancelled)
                MessageBox.Show("Data Load Cancelled");
            else
                MessageBox.Show("Data Load Finished");
            btnLoad.Enabled = true;
            btnCancel.Enabled = false;
            pBar1.Maximum = 0;
            lblStatus.Text = string.Empty;
        }
        #endregion

        #region ListBox Functions
        private void btnExclude_Click(object sender, EventArgs e)
        {
            foreach(object o in lstInclude.SelectedItems)
            {
                lstExclude.Items.Add(o);
            }
            ListBox.SelectedIndexCollection indices  = lstInclude.SelectedIndices;
            for (int i = indices.Count - 1; i >= 0; i--)
            {
                lstInclude.Items.RemoveAt(indices[i]);
            }
        }

        private void btnExcludeAll_Click(object sender, EventArgs e)
        {
            foreach (object o in lstInclude.Items)
            {
                lstExclude.Items.Add(o);
            }
            lstInclude.Items.Clear();
        }

        private void btnInclude_Click(object sender, EventArgs e)
        {
            foreach (object o in lstExclude.SelectedItems)
            {
                lstInclude.Items.Add(o);
            }
            ListBox.SelectedIndexCollection indices = lstExclude.SelectedIndices;
            for (int i = indices.Count - 1; i >= 0; i--)
            {
                lstExclude.Items.RemoveAt(indices[i]);
            }
        }

        private void btnIncludeAll_Click(object sender, EventArgs e)
        {
            foreach (object o in lstExclude.Items)
            {
                lstInclude.Items.Add(o);
            }
            lstExclude.Items.Clear();
        }
        #endregion

        #region Display LogFiles

        private void btnDeleteLog_Click(object sender, EventArgs e)
        {
            if (strDeleteLogFile != null && strDeleteLogFile != string.Empty)
            {
                System.Diagnostics.Process proc = new System.Diagnostics.Process();
                proc.StartInfo.FileName = "notepad.exe";
                proc.StartInfo.Arguments = strDeleteLogFile;
                proc.Start();
                proc.Dispose();
            }
            else
                MessageBox.Show("Delete Log File does not exist");
        }

        private void btnMigrateLog_Click(object sender, EventArgs e)
        {
            if (strLoadLogFile != null && strLoadLogFile != string.Empty)
            {
                System.Diagnostics.Process proc = new System.Diagnostics.Process();
                proc.StartInfo.FileName = "notepad.exe";
                proc.StartInfo.Arguments = strLoadLogFile;
                proc.Start();
                proc.Dispose();
            }
            else
                MessageBox.Show("Load Log File does not exist");
        }
        #endregion

        private void lblSource_Click(object sender, EventArgs e)
        {

        }

        private void button1_Click(object sender, EventArgs e)
        {
            strDestConn = txtDestination.Text;
            strSourceConn = txtSource.Text;

            using (MySqlConnection conn = new MySqlConnection(strDestConn))
            {
                conn.Open();
                MySqlCommand cmd = conn.CreateCommand();
                MySqlDataAdapter da = new MySqlDataAdapter("show tables;", conn);
                ds = new DataSet();
                da.Fill(ds);
            }

            foreach (DataRow dr in ds.Tables[0].Rows)
            {
                lstInclude.Items.Add(dr[0]);
            }
        }

        private void button2_Click(object sender, EventArgs e)
        {

        }

    }
}