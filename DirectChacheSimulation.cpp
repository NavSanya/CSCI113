#include <iostream>
#include <vector>
using namespace std;

//vectors for direct cache(starts with a D)
vector<int>DhitMiss(16);
vector<int>Dvalid(16, 0);
vector<int>Dtag(16,-1);
vector<int>DData(16);
vector<int> mainMem;



void direct(int n);
void bitprint_4bit(int v);
void bitprint(int v);
void Dprint();//to print the direct cache
void initilization();
int main()
{
	vector<int> input = { 1,4,8,5,20,17,19,56,9,11,4,43,5,6,9,17 };
	initilization();
	for (int i = 0; i < input.size(); ++i)
	{
		direct(input.at(i));
	}
	Dprint();
	return 0;
}//main()

void initilization()
{
	for (int i = 0; i < 64; i++)
	{
		mainMem.push_back(i + 5);
	}
}

void direct(int n) 
{
	int currIn = n % 16;//current index
	int currTag = n / 16;//current tag
	if ((Dtag.at(currIn) != currIn)|| (Dtag.at(currIn)==-1)) //if its different or empty=miss condition
	{
		DhitMiss.at(currIn) = 0;
	}
	else//hit case
	{
		DhitMiss.at(currIn) = 1;
	}

	DData.at(currIn) = mainMem.at(n);
	Dvalid.at(currIn) = 1;
	Dtag.at(currIn) = currTag;
}

void Dprint()
{
	cout << "Index\tValid\tTag\tData\t\t\t\tHit/Miss" << endl;
	for (int i = 0; i < 16; ++i)
	{
		bitprint_4bit(i);
		cout << "\t" << Dvalid.at(i) << "\t";
		if (Dtag.at(i) == -1)
			cout << "-";
		else
			bitprint_4bit(Dtag.at(i));
		cout << "\t";

		bitprint(DData.at(i));
		cout <<"\t"<<DhitMiss.at(i)<< endl;
	}
}

void bitprint_4bit(int v)
{
	int mask = 1;
	mask <<=3;
	for (int i = 1; i <= 4; ++i)
	{
		if ((v & mask) == 0)
			cout << "0";
		else
			cout << "1";

		v <<= 1;
	}
	
}

void bitprint(int v)
{
	int mask = 1;
	mask <<= 31;
	for (int i = 1; i <= 32; ++i)
	{
		if ((v & mask) == 0)
			cout << "0";
		else
			cout << "1";
		v <<= 1;
	}
}

