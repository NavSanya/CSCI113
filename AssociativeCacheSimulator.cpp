#include <iostream>
#include <vector>
using namespace std;

//parameters for 2-way associative cache
vector<int>AhitMiss(16);
vector<int>Avalid(16, 0);
vector<int>Atag(16, -1);
int sets[8][2];
int lru[8][2];
vector<int> mainMem;

void initilization();
void AssociativeCache(int n);
void bitprint_4bit(int v);
void bitprint(int v);
void APrint();

int main()
{
	vector<int> references = { 1, 4, 8, 5, 20, 17, 19, 56, 9, 11, 4, 43, 5, 6, 9, 17 };
	initilization();
	for (int i = 0; i < references.size(); ++i)
	{
		AssociativeCache(references.at(i));
	}
	APrint();
	return 0;
}

void initilization()
{
	for (int i = 0; i < 64; i++) 
	{
		mainMem.push_back(i + 5);
	}
}

void AssociativeCache(int n)
{
	int currSet = n % 8;
	int currIn = n % 16;
	int currTag = n / 16;
	Avalid.at(currIn) = 1;
	Atag.at(currIn) = currTag;

	if (sets[currSet][0] == 0)
	{
		sets[currSet][0] = mainMem.at(n);
		AhitMiss.at(currIn) = 0;
		lru[currSet][0] = 1;
		lru[currSet][1] = 0;
	}
	else if (sets[currSet][1] == 0)
	{
		sets[currSet][1] = mainMem.at(n);
		AhitMiss.at(currIn) = 0;
		lru[currSet][1] = 1;
		lru[currSet][0] = 0;
	}
	else if((sets[currSet][0] == mainMem.at(n))||(sets[currSet][1]== mainMem.at(n)))
	{
		AhitMiss.at(currIn) = 1;
	}
	else
	{
		if (lru[currSet][0] == 1)
		{
			sets[currSet][1] = mainMem.at(n);
			AhitMiss.at(currIn) = 0;
			lru[currSet][1] = 1;
			lru[currSet][0] = 0;
		}
		else
		{
			sets[currSet][0] = mainMem.at(n);
			AhitMiss.at(currIn) = 0;
			lru[currSet][0] = 1;
			lru[currSet][1] = 0;
		}
	}
}

void APrint()
{
	
	
		cout << "Index\tValid\tTag\tData\t\t\t\tHit/Miss" << endl;
		for (int i = 0; i < 16; ++i)
		{
			bitprint_4bit(i);
			cout << "\t" << Avalid.at(i) << "\t";
			if (Atag.at(i) == -1)
				cout << "-";
			else
				bitprint_4bit(Atag.at(i));
			cout << "\t";

			bitprint(mainMem.at(i));
			cout << "\t" << AhitMiss.at(i) << endl;
		}
}

void bitprint_4bit(int v)
{
	int mask = 1;
	mask <<= 3;
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