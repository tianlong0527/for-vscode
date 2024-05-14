#include <iostream>
#include <string>
using namespace std;
int compareVersion(string version1, string version2)
{
    int loc1, loc2;
    int num1, num2;
    bool exist1 = true, exist2 = true;
    do
    {
        loc1 = version1.find(".");
        loc2 = version2.find(".");
        if (loc1 == string::npos && version1 != "")
        {
            num1 = stoi(version1);
            exist1 = false;
        }
        else
        {
            if (version1 == "") num1 = 0;
            else num1 = stoi(version1.substr(0, loc1));
        }
        if (loc2 == string::npos && version2 != "")
        {
            num2 = stoi(version2);
            exist2 = false;
        }
        else
        {
            if (version2 == "") num2 = 0;
            else num2 = std::stoi(version2.substr(0, loc2));
        }
        if (num1 > num2)
        {
            return 1;
        }
        else if (num1 < num2)
        {
            return -1;
        }
        else
        {
            if(exist1) {
                version1 = version1.substr(loc1 + 1);
            }
            else {
                version1 = "";
            }
            if (exist2)
            {
                version2 = version2.substr(loc2 + 1);
            }
            else
            {
                version2 = "";
            }

        }
    } while (loc1 != string::npos || loc2 != string::npos);
    return 0;
}
int main() {
    string version1 = "1.0.1";
    string version2 = "1";
    cout << compareVersion(version1, version2) << endl;
    return 0;
}