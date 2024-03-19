#include <iostream>
#include <string>
#include <map>

int main(){
    std::string str1;
    while(std::cin >> str1){
        if(str1.length() != 10) std::cout << "fake" << std::endl;
        const char c[10] = str1.c_str();
        std::map<const char,int> Letterchange = {
            {"A",10},
            {"B",11},
            {"C",12},
            {"D",13},
            {"E",14},
            {"F",15},
            {"G",16},
            {"H",17},
            {"I",34},
            {"J",18},
            {"K",19},
            {"L",20},
            {"M",21},
            {"N",22},
            {"O",35},
            {"P",23},
            {"Q",24},
            {"R",25},
            {"S",26},
            {"T",27},
            {"U",28},
            {"V",29},
            {"W",32},
            {"X",30},
            {"Y",31},
            {"Z",33},
        };
        for(const auto& s : Letterchange) {
            if(c[0] == s.first){
                c[0] = s.second;
            }
        }
    }
}