package view.backing;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;

import javax.el.ValueExpression;

import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

import oracle.adf.model.BindingContext;
import oracle.adf.model.binding.DCBindingContainer;
import oracle.adf.model.binding.DCIteratorBinding;
import oracle.adf.view.rich.component.rich.RichDocument;
import oracle.adf.view.rich.component.rich.RichForm;
import oracle.adf.view.rich.component.rich.data.RichTable;
import oracle.adf.view.rich.component.rich.input.RichInputText;
import oracle.adf.view.rich.component.rich.input.RichSelectItem;
import oracle.adf.view.rich.component.rich.input.RichSelectOneChoice;
import oracle.adf.view.rich.component.rich.output.RichMessages;
import oracle.adf.view.rich.component.rich.output.RichOutputText;
import oracle.adf.view.rich.context.AdfFacesContext;
import oracle.adf.view.rich.event.QueryEvent;

import oracle.jbo.RowMatch;
import oracle.jbo.ViewObject;

public class MoviePremierAdvancedPage { 
    ///////////////////////////////////
    // These UI binding variables can be removed as these are not needed
    ///////////////////////////////////
    private RichForm f1;
    private RichDocument d1;
    private RichOutputText ot1;
    private RichTable t1;
    private RichMessages m1;
    ///////////////////////////////////
    
    private RichSelectOneChoice soc2; // This is for Month pulldown menu component
    private RichSelectOneChoice soc1; // This is for Rated filter component

    /**
     * Event listener for Rated pulldown filter.
     * This method filters movie data by selected rate such as R, PG-13, PG, or G.
     * 
     * @param queryEvent
     */ 
    public void customFilter(QueryEvent queryEvent) {
        DCBindingContainer dcBinding = (DCBindingContainer)BindingContext.getCurrent().getCurrentBindingsEntry();
        DCIteratorBinding dc = dcBinding.findIteratorBinding("MovieDataVO1Iterator");
        ViewObject vo = dc.getViewObject();
        if (getSoc1().getValue() != null && !"".equals(getSoc1().getValue())) {
            invokeEL("#{bindings.populateMovieData.execute}",new Class[0], new Object[0]);
            
            RowMatch rm = new RowMatch("Rated='" + getSoc1().getValue()+"'");
            vo.setQueryMode(ViewObject.QUERY_MODE_SCAN_VIEW_ROWS);
            vo.setRowMatch(rm);
            vo.executeQuery();
            
            AdfFacesContext.getCurrentInstance().addPartialTarget(getT1());
        }
    }
    
    /**
     * Event listener for Month pulldown menu.
     * This method invokes webserver container's populateMovieData() to populates movie data for selected month
     * 
     * @param valueChangeEvent
     */     
    public void monthChangeListener(ValueChangeEvent valueChangeEvent) {
      Object newValue = valueChangeEvent.getNewValue();
      setEL("#{pageFlowScope.month}", newValue);
      //String test = (String)evaluateEL("#{pageFlowScope.month}");
      invokeEL("#{bindings.populateMovieData.execute}",new Class[0], new Object[0]);
          
      // Clear selection from the Rated filter pulldown menu
      getSoc1().setValue("");
    }
    
    /**
     * Programmatic invocation of a method that an EL evaluates to.
     *
     * @param el EL of the method to invoke
     * @param paramTypes Array of Class defining the types of the
     * parameters
     * @param params Array of Object defining the values of the
     * parametrs
     * @return Object that the method returns
     */
    public static Object invokeEL(String el, Class[] paramTypes, Object[] params)
    {
      Object oReturn = null;
      FacesContext facesContext = FacesContext.getCurrentInstance();
      ELContext elContext = facesContext.getELContext();
      ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
      MethodExpression exp = expressionFactory.createMethodExpression(elContext, el, Object.class, paramTypes);

      try
        {
          oReturn = exp.invoke(elContext, params);
          if (oReturn instanceof Exception)
            {
              Exception ex = (Exception) oReturn;
              throw new RuntimeException(ex.getMessage(), ex);
            }
        }
      catch (ELException elex)
        {
          throw elex;
        }

      return oReturn;
    }
    
    /**
     * Programmatic evaluation of EL.
     *
     * @param el EL to evaluate
     * @return Result of the evaluation
     */
    public static Object evaluateEL(String el)
    {
      Object oReturn = null;
      FacesContext facesContext = FacesContext.getCurrentInstance();
      ELContext elContext = facesContext.getELContext();
      ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
      ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);

      try
        {
          oReturn = exp.getValue(elContext);
        }
      catch (ELException elex)
        {
          throw elex;
        }

      return oReturn;
    }
    
    /**
     * Sets a value into an EL object. Provides similar
     * functionality to
     * the &lt;af:setActionListener&gt; tag, except the
     * <code>from</code> is
     * not an EL. You can get similar behavior by using the
     * following...<br>
     * <code>setEL(<b>to</b>, evaluateEL(<b>from</b>))</code>
     *
     * @param el EL object to assign a value
     * @param val Value to assign
     */
    public static void setEL(String el, Object val)
    {
      FacesContext facesContext = FacesContext.getCurrentInstance();
      ELContext elContext = facesContext.getELContext();
      ExpressionFactory expressionFactory = facesContext.getApplication().getExpressionFactory();
      ValueExpression exp = expressionFactory.createValueExpression(elContext, el, Object.class);
      exp.setValue(elContext, val);
    }


    public void setF1(RichForm f1) {
        this.f1 = f1;
    }

    public RichForm getF1() {
        return f1;
    }

    public void setD1(RichDocument d1) {
        this.d1 = d1;
    }

    public RichDocument getD1() {
        return d1;
    }

    public void setOt1(RichOutputText ot1) {
        this.ot1 = ot1;
    }

    public RichOutputText getOt1() {
        return ot1;
    }

    public void setT1(RichTable t1) {
        this.t1 = t1;
    }

    public RichTable getT1() {
        return t1;
    }

    public void setM1(RichMessages m1) {
        this.m1 = m1;
    }

    public RichMessages getM1() {
        return m1;
    }

    public void setSoc2(RichSelectOneChoice soc2) {
        this.soc2 = soc2;
    }

    public RichSelectOneChoice getSoc2() {
        return soc2;
    }

    public void setSoc1(RichSelectOneChoice soc1) {
        this.soc1 = soc1;
    }

    public RichSelectOneChoice getSoc1() {
        return soc1;
    }
}
