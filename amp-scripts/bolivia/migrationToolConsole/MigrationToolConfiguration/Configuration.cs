using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using System.Data.Common;

namespace MigrationToolConfiguration
{
    public partial class Configuration : Form
    {
        public Configuration()
        {
            InitializeComponent();
            InitializeForm();
        }

        private void InitializeForm()
        {
            DataTable providers = GetProviderFactoryClasses();
            sourceDataProviders.ValueMember = "InvariantName";
            sourceDataProviders.DisplayMember = "Name";
            
            sourceDataProviders.DataSource = providers.DefaultView;

            
        }




        static DataTable GetProviderFactoryClasses()
        {
            // Retrieve the installed providers and factories.
            DataTable table = DbProviderFactories.GetFactoryClasses();

            // Display each row and column value.
            foreach (DataRow row in table.Rows)
            {
                foreach (DataColumn column in table.Columns)
                {
                    Console.WriteLine(row[column]);
                }
            }
            return table;
        }


        private void radioButton2_CheckedChanged(object sender, EventArgs e)
        {
            enableServerSettings();
            disableConnectionString();
        }

        private void disableConnectionString()
        {
            sourceConnectionString.Enabled = false;
        }

        private void enableServerSettings()
        {
            sourceDatabaseName.Enabled = true;
            sourceServerHost.Enabled = true;
            sourceServerPort.Enabled = true;
            sourceUsername.Enabled = true;
            sourcePassword.Enabled = true;
        }
        private void radioButton1_CheckedChanged(object sender, EventArgs e)
        {
            enableConnectionString();
            disableServerSettings();
        }

        private void disableServerSettings()
        {
            sourceDatabaseName.Enabled = false;
            sourceServerHost.Enabled = false;
            sourceServerPort.Enabled = false;
            sourceUsername.Enabled = false;
            sourcePassword.Enabled = false;
        }

        private void enableConnectionString()
        {
            sourceConnectionString.Enabled = true;
        }

        private void btnCancel_Click(object sender, EventArgs e)
        {
            Close();
        }

        private void button1_Click(object sender, EventArgs e)
        {
            try
            {
                DbProviderFactory factory = DbProviderFactories.GetFactory(sourceDataProviders.SelectedValue.ToString());

                DbConnection connection = factory.CreateConnection();

                connection.ConnectionString = GetConnectionString();
                connection.Open();
                MessageBox.Show("Test succesful.");
                connection.Close();
            }
            catch (Exception ex)
            {
                MessageBox.Show("Test failed");
            }

        }

        private string GetConnectionString()
        {
            if (radioButtonSource1.Checked)
            {
                return sourceConnectionString.Text;
            }
            else
            {
                return "SERVER = " + sourceServerHost.Text + ";UID=" + sourceUsername.Text + ";PWD=" + sourcePassword.Text + ";DATABASE=" + sourceDatabaseName.Text + ";PORT=" + sourceServerPort.Text + ";";
            }
        }

        private void radioButton3_CheckedChanged(object sender, EventArgs e)
        {

        }

        private void textBox12_TextChanged(object sender, EventArgs e)
        {

        }

        private void groupBox2_Enter(object sender, EventArgs e)
        {

        }

        private void destinationUsername_TextChanged(object sender, EventArgs e)
        {

        }

        private void buttonTestDestination_Click(object sender, EventArgs e)
        {

        }


    }
}
