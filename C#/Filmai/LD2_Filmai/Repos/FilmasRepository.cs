using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using MySql.Data.MySqlClient;
using LD2_Filmai.Models;

namespace LD2_Filmai.Repos
{
    public class FilmasRepository
    {
        public List<Filmas> getFilmai()
        {
            List<Filmas> filmai = new List<Filmas>();

            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = "select * from filmai";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlConnection.Open();
            MySqlDataAdapter mda = new MySqlDataAdapter(mySqlCommand);
            DataTable dt = new DataTable();
            mda.Fill(dt);
            mySqlConnection.Close();

            foreach (DataRow item in dt.Rows)
            {
                filmai.Add(new Filmas
                {
                    id = Convert.ToInt32(item["id"]),
                    pavadinimas = Convert.ToString(item["pavadinimas"]),
                    isleidimoMetai = Convert.ToInt32(item["isleidimo_metai"]),
                    trukme = (TimeSpan)item["trukme"],
                    zanras = Convert.ToString(item["zanras"])
                });
            }
            return filmai;
        }

        public Filmas getFilmas(int id)
        {
            Filmas filmas = new Filmas();
            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = "select * from filmai where id=" + id;
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlConnection.Open();
            MySqlDataAdapter mda = new MySqlDataAdapter(mySqlCommand);
            DataTable dt = new DataTable();
            mda.Fill(dt);
            mySqlConnection.Close();

            foreach (DataRow item in dt.Rows)
            {
                filmas.id = Convert.ToInt32(item["id"]);
                filmas.pavadinimas = Convert.ToString(item["pavadinimas"]);
                filmas.isleidimoMetai = Convert.ToInt32(item["isleidimo_metai"]);
                filmas.trukme = (TimeSpan)item["trukme"];
                filmas.zanras = Convert.ToString(item["zanras"]);
            }

            return filmas;
        }

        public bool addFilmas(Filmas filmas)
        {
            try
            {
                string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
                MySqlConnection mySqlConnection = new MySqlConnection(conn);
                string sqlquery = @"INSERT INTO filmai(id,pavadinimas,isleidimo_metai,trukme,zanras)VALUES(?id,?pavadinimas,?isleidimo_metai,?trukme,?zanras);";
                MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
                mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = filmas.id;
                mySqlCommand.Parameters.Add("?pavadinimas", MySqlDbType.VarChar).Value = filmas.pavadinimas;
                mySqlCommand.Parameters.Add("?isleidimo_metai", MySqlDbType.Int32).Value = filmas.isleidimoMetai;
                mySqlCommand.Parameters.Add("?trukme", MySqlDbType.Time).Value = filmas.trukme;
                mySqlCommand.Parameters.Add("?zanras", MySqlDbType.VarChar).Value = filmas.zanras;
                mySqlConnection.Open();
                mySqlCommand.ExecuteNonQuery();
                mySqlConnection.Close();
                return true;
            }
            catch (Exception)
            {
                return false;
            }
        }

        public void deleteFilmas(int id)
        {
            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"DELETE FROM filmai where id=?id";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = id;
            mySqlConnection.Open();
            mySqlCommand.ExecuteNonQuery();
            mySqlConnection.Close();
        }

        public bool updateFilmas(Filmas filmas)
        {
            try
            {
                string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
                MySqlConnection mySqlConnection = new MySqlConnection(conn);
                string sqlquery = @"UPDATE filmai a SET a.pavadinimas=?pavadinimas,
                                                        a.isleidimo_metai=?isleidimo_metai,
                                                        a.trukme=?trukme,
                                                        a.zanras=?zanras 
                                                        WHERE a.id=?id";
                MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
                mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = filmas.id;
                mySqlCommand.Parameters.Add("?pavadinimas", MySqlDbType.VarChar).Value = filmas.pavadinimas;
                mySqlCommand.Parameters.Add("?isleidimo_metai", MySqlDbType.Int32).Value = filmas.isleidimoMetai;
                mySqlCommand.Parameters.Add("?trukme", MySqlDbType.Time).Value = filmas.trukme;
                mySqlCommand.Parameters.Add("?zanras", MySqlDbType.VarChar).Value = filmas.zanras;
                mySqlConnection.Open();
                mySqlCommand.ExecuteNonQuery();
                mySqlConnection.Close();
                return true;
            }
            catch
            {
                return false;
            }
        }
    }
}