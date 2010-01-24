/*
   INPUT FILE FORMAT:

   [offset] [type]          [value]          [description] 
   0000     32 bit integer  0x00000803(2051) magic number 
   0004     32 bit integer  60000            number of images 
   0008     32 bit integer  28               number of rows 
   0012     32 bit integer  28               number of columns 
   0016     unsigned byte   ??               pixel 
   0017     unsigned byte   ??               pixel 
   ........ 
   xxxx     unsigned byte   ??               pixel

   [offset] [type]          [value]          [description] 
   0000     32 bit integer  0x00000801(2049) magic number (MSB first) 
   0004     32 bit integer  60000            number of items 
   0008     unsigned byte   ??               label 
   0009     unsigned byte   ??               label 
   ........ 
   xxxx     unsigned byte   ??               label


   OUTPUT FILE FORMAT:
	each line consists of integers seperated by ','
	each integer is the grey level of each pixel.

useage:
	MNISTtoTxt DATA_SET_FLIE DATA_SET_LABEL_FILE 
	
	Output into "out.txt" file.
	Enter number of instance to convert when required.
   */
#include<stdio.h>
#include<string.h>
FILE* input,*ans,*out;
int lsb_to_int(unsigned char*s,int n){
	int res=0;
	for(int i=0;i<n;i++)
		res|=(s[i]<<i);
	return res;
}
void reverse(unsigned char*s,int n){
	for(int i=0,j=n-1;i<j;i++,j--){
		unsigned char t=s[i];
		s[i]=s[j];
		s[j]=t;		 
	}	 
}
int msb_to_int(unsigned char*s,int n){
	unsigned char t[10];
	memcpy(t,s,n);
	reverse(t,n);
	return lsb_to_int(t,n);
}
int min(int a,int b){return a<b?a:b;}
int abs(int a){return a>0?a:-a;}
int main(int argc,char** args){
	int target;
	if(argc!=3){
		printf("data set filename, label file name filename required\n");
		return 1;
	}
	input=fopen(args[1],"rb");
	ans=fopen(args[2],"rb");
	out=fopen("out.txt","w");	
	if(!input || !ans || !out){
		printf("fail to open file\n");
		return 1;
	}
	unsigned char s[10];
	fread(s,1,4,input);
	fread(s,1,4,input);
	int num=msb_to_int(s,4);
	fread(s,1,4,input);
	int row=msb_to_int(s,4);
	fread(s,1,4,input);;
	int col=msb_to_int(s,4);
	fread(s,1,8,ans);
	printf("Input the number of instance you want to read:");
	scanf("%d",&num);
	for(int i=0;i<num;i++){
		for(int j=0;j<row;j++){
			for(int k=0;k<col;k++){
				if(fread(s,1,1,input)!=1)
					return 1;
				fprintf(out,"%d,",msb_to_int(s,1));
			}
		}
		fread(s,1,1,ans);
		fprintf(out,"%d\n",msb_to_int(s,1));
	}
	return 0;
}
