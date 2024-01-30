using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using MySql.Data.MySqlClient;
using LD2_Filmai.Models;

namespace LD2_Filmai.Repos
{
    public class SaskaitaRepository
    {
        public List<Saskaita> getSaskaitos()
        {
            List<Saskaita> saskaitos = new List<Saskaita>();

            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = "select * from saskaitos";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlConnection.Open();
            MySqlDataAdapter mda = new MySqlDataAdapter(mySqlCommand);
            DataTable dt = new DataTable();
            mda.Fill(dt);
            mySqlConnection.Close();

            foreach (DataRow item in dt.Rows)
            {
                saskaitos.Add(new Saskaita
                {
                    nr = Convert.ToInt32(item["nr"]),
                    data = Convert.ToDateTime(item["data"]),
                    suma = Convert.ToDouble(item["suma"])
                });
            }

            return saskaitos;
        }
    }
}