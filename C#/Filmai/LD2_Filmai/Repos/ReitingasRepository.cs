using System;
using System.Collections.Generic;
using System.Configuration;
using System.Data;
using MySql.Data.MySqlClient;
using LD2_Filmai.ViewModels;

namespace LD2_Filmai.Repos
{
    public class ReitingasRepository
    {
        public List<ReitingasViewModel> getReitingai()
        {
            List<ReitingasViewModel> reitingai = new List<ReitingasViewModel>();

            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"SELECT m.id, m.metacritic, m.rotten_tomatoes, m.imdb, mm.pavadinimas AS filmas
                                FROM reitingai m
                                LEFT JOIN filmai mm ON mm.id=m.fk_filmas";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlConnection.Open();
            MySqlDataAdapter mda = new MySqlDataAdapter(mySqlCommand);
            DataTable dt = new DataTable();
            mda.Fill(dt);
            mySqlConnection.Close();

            foreach (DataRow item in dt.Rows)
            {
                reitingai.Add(new ReitingasViewModel
                {
                    id = Convert.ToInt32(item["id"]),
                    metacritic = Convert.ToInt32(item["metacritic"]),
                    rottenTomatoes = Convert.ToInt32(item["rotten_tomatoes"]),
                    imdb = Convert.ToDouble(item["imdb"]),
                    filmas = Convert.ToString(item["filmas"])
                });
            }

            return reitingai;
        }

        public ReitingasEditViewModel getReitingas(int id)
        {
            ReitingasEditViewModel reitingas = new ReitingasEditViewModel();

            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"SELECT m.* 
                                FROM reitingai m WHERE m.id=" + id;
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlConnection.Open();
            MySqlDataAdapter mda = new MySqlDataAdapter(mySqlCommand);
            DataTable dt = new DataTable();
            mda.Fill(dt);
            mySqlConnection.Close();

            foreach (DataRow item in dt.Rows)
            {
                reitingas.id = Convert.ToInt32(item["id"]);
                reitingas.metacritic = Convert.ToInt32(item["metacritic"]);
                reitingas.rottenTomatoes = Convert.ToInt32(item["rotten_tomatoes"]);
                reitingas.imdb = Convert.ToDouble(item["imdb"]);
                reitingas.rogerEbert = Convert.ToInt32(item["roger_ebert"]);
                reitingas.theGuardian = Convert.ToInt32(item["the_guardian"]);
                reitingas.fk_filmas = Convert.ToInt32(item["fk_filmas"]);
            }

            return reitingas;
        }

        public bool addReitingas(ReitingasEditViewModel reitingas)
        {
            try
            {
                string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
                MySqlConnection mySqlConnection = new MySqlConnection(conn);
                string sqlquery = @"INSERT INTO reitingai(id,metacritic,rotten_tomatoes,imdb,roger_ebert,the_guardian,fk_filmas)
                                    VALUES(?id,?metacritic,?rotten_tomatoes,?imdb,?roger_ebert,?the_guardian,?filmas)";
                MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
                mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = reitingas.id;
                mySqlCommand.Parameters.Add("?metacritic", MySqlDbType.Int32).Value = reitingas.metacritic;
                mySqlCommand.Parameters.Add("?rotten_tomatoes", MySqlDbType.Int32).Value = reitingas.rottenTomatoes;
                mySqlCommand.Parameters.Add("?imdb", MySqlDbType.Double).Value = reitingas.imdb;
                mySqlCommand.Parameters.Add("?roger_ebert", MySqlDbType.Int32).Value = reitingas.rogerEbert;
                mySqlCommand.Parameters.Add("?the_guardian", MySqlDbType.Int32).Value = reitingas.theGuardian;
                mySqlCommand.Parameters.Add("?filmas", MySqlDbType.VarChar).Value = reitingas.fk_filmas;
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

        public bool updateReitingas(ReitingasEditViewModel reitingas)
        {
            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"UPDATE reitingai a 
                                SET a.metacritic=?metacritic,
                                    a.rotten_tomatoes=?rotten_tomatoes,
                                    a.imdb=?imdb,
                                    a.roger_ebert=?roger_ebert,
                                    a.the_guardian=?the_guardian,
                                    a.fk_filmas=?filmas 
                                    WHERE a.id=?id";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = reitingas.id;
            mySqlCommand.Parameters.Add("?metacritic", MySqlDbType.Int32).Value = reitingas.metacritic;
            mySqlCommand.Parameters.Add("?rotten_tomatoes", MySqlDbType.Int32).Value = reitingas.rottenTomatoes;
            mySqlCommand.Parameters.Add("?imdb", MySqlDbType.Double).Value = reitingas.imdb;
            mySqlCommand.Parameters.Add("?roger_ebert", MySqlDbType.Int32).Value = reitingas.rogerEbert;
            mySqlCommand.Parameters.Add("?the_guardian", MySqlDbType.Int32).Value = reitingas.theGuardian;
            mySqlCommand.Parameters.Add("?filmas", MySqlDbType.VarChar).Value = reitingas.fk_filmas;
            mySqlConnection.Open();
            mySqlCommand.ExecuteNonQuery();
            mySqlConnection.Close();
            return true;
        }

        public void deleteReitingas(int id)
        {
            string conn = ConfigurationManager.ConnectionStrings["MysqlConnection"].ConnectionString;
            MySqlConnection mySqlConnection = new MySqlConnection(conn);
            string sqlquery = @"DELETE FROM reitingai where id=?id";
            MySqlCommand mySqlCommand = new MySqlCommand(sqlquery, mySqlConnection);
            mySqlCommand.Parameters.Add("?id", MySqlDbType.Int32).Value = id;
            mySqlConnection.Open();
            mySqlCommand.ExecuteNonQuery();
            mySqlConnection.Close();
        }
    }
}