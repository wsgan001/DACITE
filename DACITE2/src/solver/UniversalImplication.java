package solver;

import static choco.Choco.gt;
import static choco.Choco.leq;
import static choco.Choco.lt;
import static choco.Choco.implies;
import static choco.Choco.and;
import static choco.Choco.minus;
import static choco.Choco.or;
import static choco.Choco.makeIntVar;
import static choco.Choco.not;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import choco.cp.model.CPModel;
import choco.cp.solver.CPSolver;
import choco.kernel.model.Model;
import choco.kernel.model.constraints.Constraint;
import choco.kernel.model.variables.integer.IntegerVariable;
import choco.kernel.solver.Solver;

import com.constraint.ConstraintBuilder;

import expression.CBParser;

public class UniversalImplication {

	//	Constraint left;
	//	Constraint right;

	ArrayList<Object> left;
	ArrayList<Object> right;

	public Map<String, Object> symStringToVar = new HashMap<String,Object>();; 
	
	
	public UniversalImplication(){}

	public UniversalImplication(ConstraintBuilder left, ConstraintBuilder right){

		System.out.println("parse left...."  + left);
		this.left =  CBParser.parse(left, symStringToVar);
		System.out.println( "size of map " + CBParser.symStringToVar.size());
		System.out.println("parse right...." + right );
		this.right = CBParser.parse(right, symStringToVar);
		System.out.println( "size of map " + CBParser.symStringToVar.size());


		//		Object leftO = CBParser.parse(left);
		//		assert(leftO instanceof Constraint);
		//		this.left = (Constraint) leftO;
		//
		//		Object rightO = CBParser.parse(right);
		//		assert(rightO instanceof Constraint);
		//		this.right = (Constraint) rightO;

		/*
		//testing
		Model m = new CPModel();
		Solver s = new CPSolver();

		Object pAge = CBParser.symStringToVar.get("PERSON.AGE");		
		assert(pAge instanceof IntegerVariable);
		IntegerVariable iage = (IntegerVariable) pAge;
		//IntegerVariable age2 = makeIntVar("age2", -100, 100);

		//Constraint c2 =  gt( 20, iage);  //  20 > age
		Constraint c2 = lt(iage, 20); // age < 20		

		//m.addConstraint(not(implies( c2, leftC)));
		m.addConstraint(not(implies( leftC, c2)));
		s.read(m);
		//s.setTimeLimit(10);

		Boolean solved = s.solve();
		boolean feasible = s.isFeasible();
		System.out.println("Solved: " + solved);
		System.out.println("Feasible: " + feasible);

		//System.out.print(s.getVar(v).getVal());
		System.out.print("solve : " + !solved);
		 */
	}


	@SuppressWarnings("deprecation")
	public boolean valid(){
		Model m = new CPModel();
		Solver s = new CPSolver();

		//m.addConstraint(not(implies( c2, leftC)));
		//m.addConstraint(not(implies(this.left, this.right)));
		//m.addConstraint(and(this.left, not(this.right)));
		
		Constraint cleft = null;

		for(Object o : this.left)
		{
			assert(o instanceof Constraint);
			System.out.println("o : " + o.toString());
			if(cleft == null)
				cleft = (Constraint)o;
			else
				cleft = and(cleft, (Constraint)o);
		}

		//System.out.println("cleft : " + cleft.toString());
		//m.addConstraint(cleft);
		
		Constraint rC = null;
		for(Object o : this.right)
		{
			assert(o instanceof Constraint);
			if(rC == null)
				rC = (Constraint)o;
			else
				rC = and(rC, (Constraint)o);
		}

		//System.out.println("rC : " + rC.toString());
		//m.addConstraint(not(rC));
		m.addConstraint(not(implies(cleft, rC)));
		

//		Object pAge = CBParser.symStringToVar.get("PERSON.AGE");
//		System.out.println("Hashcode: " + pAge.hashCode());
//		assert(pAge instanceof IntegerVariable);
//		IntegerVariable iage = (IntegerVariable) pAge;
//		
//		Constraint c3 =  leq(minus(iage, 30), 0);  // age <= 30
//		m.addConstraint(not(c3));
		
		
		System.out.println("solve left : " + this.left);
		System.out.println("solve right : " + this.right);
		s.read(m);
		s.setTimeLimit(100);

		boolean solved = s.solve();
		boolean feasible = s.isFeasible();
		System.out.println("Feasible: " + feasible);

//		Object pAge = CBParser.symStringToVar.get("PERSON.AGE");		
//		assert(pAge instanceof IntegerVariable);
//		IntegerVariable iage = (IntegerVariable) pAge;
//		System.out.println("** " + s.getVar(iage).getValue());

		//boolean feasible = s.isFeasible();
		//System.out.println("Solved: " + solved);
		//System.out.println("Feasible: " + feasible);
		//System.out.print("solve : " + !solved);
		return !solved;
	}
}
