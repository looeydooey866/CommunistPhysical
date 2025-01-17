public class run{
    public static void main(String[] args){
        Interactor x = new Interactor();
        while (!x.terminated){
            x.query();
            x.acceptQuery();
        }
    }
} /*


                                           ######################################
                                           ================SYNTAX================
                                           ######################################


store coord [name] [x] [y]
    -> Stores the coordinate values {x,y} in the variable [name] in the storage.

store polar [name] [mag] [deg] [deg/rad]
    -> Stores the polar coordinate values {mag,deg} in the variable [name] in the storage, in either degree or radian units.

store equation [equation] (e.g. "store equation a = b + c")
    -> Stores the result of the equation in the variable [name] in the storage.
    -> Supports -[vector name] and + and - operations. (e.g. "a = -b + c - -c") [Vector names and signs must be space-separated!]

retrieve [name] coord
    -> Prints out the coordinate values of stored variable [name].

retrieve [name] polar [deg/rad]
    -> Prints out the polar coordinate values of stored variable [name], in either degree or radian units.

retrieve [name] both
    -> Prints out both polar and rectangular form of the vector [name].

list
    -> Prints out all vector names that are stored in the database.

thanks
    -> Thanks the servant.

terminate
    -> Stops the program.

Happy Vectoring!
 */