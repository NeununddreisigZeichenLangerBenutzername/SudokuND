import java.util.HashMap;
import java.util.LinkedList;
class SudokuND{
    int dimension;
    int volume;
    LinkedList<Integer>valueRange;
    LinkedList<Integer>leftFields;
    LinkedList<Integer>pressing;
    HashMap<Integer,Integer>solution;
    HashMap<Integer,Integer>puzzle;
    void initialize(int dimension,int volume){
        this.dimension=dimension;
        this.volume=volume;
        this.valueRange=new LinkedList<>();
        for(int a=0;a<Math.pow(this.volume,this.dimension);a++){
            this.valueRange.add(a);
        }
        this.leftFields=new LinkedList<>();
        for(int a=0;a<Math.pow(this.volume,this.dimension*this.dimension);a++){
            this.leftFields.add(a);
        }
        this.pressing=new LinkedList<>();
        this.solution=new HashMap<>();
        this.puzzle=new HashMap<>();
    }
    void setField(int field,int value){
        System.out.println("]o[");
        this.secureField(field,value);
        this.puzzle.put(field,value);
    }
    void secureField(int field,int value){
        this.solution.put(field,value);
        if(this.pressing.contains(field)){
            this.pressing.remove(new Integer(field));
        }
        this.leftFields.remove(new Integer(field));
        System.out.println(field+","+value+","+this.leftFields);
        System.out.println(this.pressing);
        for(int a=0;a<Math.pow(this.volume,this.dimension);a++){
            String line="";
            for(int b=0;b<Math.pow(this.volume,this.dimension);b++){
                if(this.solution.containsKey(a*(int)Math.pow(this.volume,this.dimension)+b)){
                    line=line+"["+this.solution.get(a*(int)Math.pow(this.volume,this.dimension)+b)+"]";
                }else{
                    line=line+"[ ]";
                }
            }
            System.out.println(line);
        }
        this.secureDeterminedFields(field);
    }
    LinkedList<Integer>getBlock(int field){
        int fieldDifBlock=field;
        LinkedList<Integer>block=new LinkedList<>();
        for(int a=0;a<this.dimension;a++){
            fieldDifBlock=fieldDifBlock-(int)((field%(this.volume*Math.pow(this.volume,this.dimension*(a))))/Math.pow(this.volume,this.dimension*a))*(int)Math.pow(this.volume,this.dimension*a);
        }
        block.add(fieldDifBlock);
        for(int a=0;a<this.dimension;a++){
            for(int b=block.size();b>0;b--){
                for(int c=1;c<this.volume;c++){
                    block.add(block.get(b-1)+c*(int)Math.pow(this.volume,this.dimension*a));
                }
            }
        }
        return block;
    }
    LinkedList<Integer>getRow(int field,int direction){
        int fieldDifRow=field-(int)((field%Math.pow(this.volume,this.dimension*(direction+1)))/Math.pow(this.volume,this.dimension*direction))*(int)Math.pow(this.volume,this.dimension*direction);
        LinkedList<Integer>row=new LinkedList<>();
        for(int a=0;a<Math.pow(this.volume,this.dimension);a++){
            row.add(fieldDifRow+a*(int)Math.pow(this.volume,this.dimension*direction));
        }
        return row;
    }
    LinkedList<Integer>unite(LinkedList<Integer>list1,LinkedList<Integer>list2){
        LinkedList<Integer>list=new LinkedList<>();
        for(int a=0;a<list1.size();a++){
            if(!list.contains(list1.get(a))){
                list.add(list1.get(a));
            }
        }
        for(int a=0;a<list2.size();a++){
            if(!list.contains(list2.get(a))){
                list.add(list2.get(a));
            }
        }
        return list;
    }
    LinkedList<Integer>getRelevantFields(int field){
        LinkedList<Integer>relevantFields=this.getBlock(field);
        for(int a=0;a<this.dimension;a++){
            relevantFields=this.unite(relevantFields,this.getRow(field,a));
        }
        return relevantFields;
    }
    LinkedList<Integer>getBlockedValues(int field){
        LinkedList<Integer>relevantFields=this.getRelevantFields(field);
        LinkedList<Integer>blockedValues=new LinkedList<>();
        for(int a=0;a<relevantFields.size();a++){
            if(this.solution.containsKey(relevantFields.get(a))){
                blockedValues.add(solution.get(relevantFields.get(a)));
            }
        }
        return blockedValues;
    }
    LinkedList<Integer>without(LinkedList<Integer>list1,LinkedList<Integer>list2){
        LinkedList<Integer>list=new LinkedList<>(list1);
        for(int a=0;a< list2.size();a++){
            list.remove(list2.get(a));
        }
        return list;
    }
    LinkedList<Integer>getLeftValues(int field){
        LinkedList<Integer>leftValues=new LinkedList<>();
        if(!this.solution.containsKey(field)) {
            leftValues = without(this.valueRange, this.getBlockedValues(field));
        }
        return leftValues;
    }
    boolean secureObviousFieldsBlock(){
        boolean hit=false;
        int currentField;
        LinkedList<Integer>block=getBlock(0);
        LinkedList<Integer>anchors=new LinkedList<>();
        LinkedList<Integer>fields;
        for(int a=0;a<block.size();a++){
            anchors.add(block.get(a)*3);
        }
        for(int a=0;a<this.valueRange.size();a++){
            for(int b=0;b<anchors.size();b++){
                fields=new LinkedList<>();
                for(int c=0;c<block.size();c++){
                    currentField=anchors.get(b)+block.get(c);
                    if(this.getLeftValues(currentField).contains(a)){
                        fields.add(currentField);
                    }
                }
                if(fields.size()==1){
                    System.out.println("]+[");
                    this.secureField(fields.get(0),a);
                    hit=true;
                }
            }
        }
        return hit;
    }
    boolean secureObviousFieldsRow(){
        boolean hit=false;
        LinkedList<Integer>area;
        LinkedList<Integer>row;
        LinkedList<Integer>fields;
        for(int a=0;a<this.dimension;a++){
            area=new LinkedList<>();
            area.add(0);
            for(int b=0;b<this.dimension;b++){
                if(b!=a){
                    for(int c=area.size();c>0;c--){
                        area=this.unite(area,this.getRow(area.get(c-1),b));
                    }
                }
            }
            for(int b=0;b<area.size();b++){
                row=this.getRow(area.get(b),a);
                for(int c=0;c<valueRange.size();c++){
                    fields=new LinkedList<>();
                    for(int d=0;d<row.size();d++){
                        if(this.getLeftValues(row.get(d)).contains(c)){
                            fields.add(row.get(d));
                        }
                    }
                    if(fields.size()==1){
                        System.out.println("]+[");
                        this.secureField(fields.get(0),c);
                        hit=true;
                    }
                }
            }
        }
        return hit;
    }
    boolean secureObviousFields(){
        return(this.secureObviousFieldsBlock()||this.secureObviousFieldsRow());
    }
    LinkedList<Integer>intersection(LinkedList<Integer>list1,LinkedList<Integer>list2){
        LinkedList<Integer>list=new LinkedList<>();
        for(int a=list1.size();a>0;a--){
            if(list2.contains(list1.get(a-1))){
                list.add(list1.get(a-1));
            }
        }
        return list;
    }
    LinkedList<Integer>unsolved(LinkedList<Integer>list){
        for(int a=list.size();a>0;a--){
            if(this.solution.containsKey(list.get(a-1))){
                list.remove(a-1);
            }
        }
        return list;
    }
    void secureDeterminedFields(int field){
        LinkedList<Integer>relevantFields=this.unsolved(this.getRelevantFields(field));
        LinkedList<Integer>leftFields;
        LinkedList<Integer>currentlyRelevantFields;
        LinkedList<Integer>currentlyLeftFields;
        for(int a=0;a<relevantFields.size();a++){
            leftFields=this.getLeftValues(relevantFields.get(a));
            if(leftFields.size()==1){
                System.out.println("]-[");
                this.secureField(relevantFields.get(a),leftFields.get(0));
            }else if(leftFields.size()<=this.volume){
                for(int b=0;b<this.dimension;b++){
                    currentlyRelevantFields=this.unsolved(this.intersection(this.getBlock(relevantFields.get(a)),this.getRow(relevantFields.get(a),b)));
                    currentlyLeftFields=new LinkedList<>();
                    for(int c=0;c<currentlyRelevantFields.size();c++){
                        currentlyLeftFields=this.unite(currentlyLeftFields,this.getLeftValues(currentlyRelevantFields.get(c)));
                    }
                    if(!currentlyLeftFields.isEmpty()&&currentlyRelevantFields.size()==currentlyLeftFields.size()){
                        if(!this.pressing.contains(relevantFields.get(a))){
                            this.pressing.add(relevantFields.get(a));
                        }
                    }
                }
            }
        }

    }
    int getValue(int field){
        if(this.getLeftValues(field).isEmpty()){
            System.out.println("fail "+field);
        }
        return this.getLeftValues(field).get((int)Math.ceil(Math.random()*(this.getLeftValues(field).size()-1)));
    }
    void putField(int field){
        this.setField(field,getValue(field));
        while(secureObviousFields());
    }
    int getField(){
        int field;
        if(!this.pressing.isEmpty()){
            field=this.pressing.get((int)Math.ceil(Math.random()*(this.pressing.size()-1)));
        }else{
            field=this.leftFields.get((int)Math.ceil(Math.random()*(this.leftFields.size()-1)));
        }
        return field;
    }
    void generate(){
        while(!this.leftFields.isEmpty()){
            this.putField(this.getField());
        }
    }
    SudokuND(int dimension,int volume){
        this.initialize(dimension,volume);
        this.generate();
    }
}
