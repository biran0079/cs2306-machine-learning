/*
 *	Convert Semeion dataset into arff format. Output into "Semeion.arff" file
 *	Parameter:
 *		FILE_NAME: optional, the filename of input file. Default value: semeion.data
 *	Return:
 *		0 if normal,
 *		1 if cannot open input or output file.
 * */
#include<stdio.h>
#include<string.h>
int main(int argc,char** argv){
	char path[200];
	if(argc!=2){
		strcpy(path,"semeion.data");
	}else{
		strcpy(path,argv[1]);
	}
	if(!freopen(path,"r",stdin) || 
			!freopen("Semeion.arff","w",stdout)){
		printf("cannot open file\n");
		return 1;
	}
	printf("@relation Semeion\n");
	for(int i=0;i<16*16;i++)
		printf("@attribute a%d numeric\n",i);
	printf("@attribute ans {0,1,2,3,4,5,6,7,8,9}\n");
	printf("@data\n");
	double t;
	int x;
	while(1){
		for(int i=0;i<16*16;i++){
			if(scanf("%lf",&t)==EOF)return 0;
			printf("%d,",t==0?0:1);
		}
		for(int i=0;i<=9;i++){
			scanf("%d",&x);
			if(x){
				printf("%d\n",i);
			}
		}
	}
	return 0;
}
