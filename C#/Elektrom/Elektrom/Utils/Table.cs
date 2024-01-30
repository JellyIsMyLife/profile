using Elektrom.Models;
using Microsoft.EntityFrameworkCore;
using System.Security.Policy;

namespace Elektrom.Utils
{
    public class Table
    {
        public readonly int rows = 3;
        public readonly int cols = 5;
        public int page;
        public int maxPages;
        public List<List<List<Prekes>>> items = new List<List<List<Prekes>>>();
        public List<Kategorijos> cat;
        public string order = "";
        public Table(List<Prekes> list)
        {
            if (list == null) return;
            list = list.Where(x => x.Rodymas).ToList();
            for (int k = 0; k < (double)list.Count / (rows * cols); k++)
            {
                List<List<Prekes>> page = new List<List<Prekes>>();
                for (int i = 0; i < rows && i * cols < list.Count; i++)
                {
                    var row = new List<Prekes>();
                    for (int j = 0; j < cols && k * cols * rows + i * cols + j < list.Count; j++)
                    {
                        row.Add(list[k * cols * rows + i * cols + j]);
                    }
                    page.Add(row);
                }
                items.Add(page);
                maxPages = k+1;
            }
        }

        public List<List<Prekes>> getPage(int index)
        {
            if(index > items.Count)
            {
                return new List<List<Prekes>>();
            }
            return items[index - 1];
        }

        public void Categories(List<Kategorijos> c)
        {
            cat = c;
        }
    }
}