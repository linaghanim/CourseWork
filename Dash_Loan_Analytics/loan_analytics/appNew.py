from Helper import *
from Loan import *
from LoanPortfolio import *
from LoanImpacts import *
from Tests.Test_Loans import *
import dash
import dash_table
import dash_core_components as dcc
import dash_bootstrap_components as dbc
import dash_html_components as html
import plotly.express as px
import plotly.graph_objects as go
import pandas as pd
#pd.options.plotting.backend = "plotly"
from dash.dependencies import Input, Output, State

app = dash.Dash(external_stylesheets=[dbc.themes.FLATLY])
loans = LoanPortfolio()

#Gather Loan Info from User
inputs = dbc.Card(
    [
        dbc.Col(html.H5(children = "Enter Information for Each Loan")),
        dbc.FormGroup(
            [
                dcc.Input(id="input1", placeholder='Principal', type='number', min = 0),
                dcc.Input(id="input2", placeholder='Rate', type='text'),
                dcc.Input(id="input3", placeholder='Min Payment', type='number', min = 0),
                dcc.Input(id="input4", placeholder='Extra Payment', type='number', min = 0)
            ],
        ),
        dbc.Button(id='buttonSearch', n_clicks=0, children='Submit', color = "primary",),
    ],
     body=True
)

#Individual Schedules
individual_card = dbc.Card(
    [
        dbc.Col(html.H5(children = "Individual Loan Schedule(s):")),
        dbc.FormGroup(
            [
                    html.Div(id="output-individual")
            ],
        )
    ],
    body=True
)

#Portfolio Schedule
port_card = dbc.Card(
    [
        dbc.Col(html.H5(children = "Portfolio Schedule:")),
        dbc.FormGroup(
            [
                    html.Div(id="output-portfolio")
            ],
        )
    ],
    body=True
)

#Contribution Inputs
contributions_input = dbc.Card(
    [
        dbc.Col(html.H5(children = "Enter All Extra Contributions")),
        dbc.FormGroup(
            [
                dcc.Input(id="cont-1", placeholder='Contribution 1', type='number', min = 0, value=0),
                dcc.Input(id="cont-2", placeholder='Contribution 2', type='number', min = 0, value=0),
                dcc.Input(id="cont-3", placeholder='Contribution 3', type='number', min = 0, value=0),
                dcc.Input(id="cont-4", placeholder='Contribution 4', type='number', min = 0, value=0),
                dcc.Input(id="cont-5", placeholder='Contribution 5', type='number', min = 0, value=0)
            ],
        ),
        dbc.Button(id='buttonContributions', n_clicks=0, children='Submit', color = "primary"),
    ],
     body=True
)

#Contribution Results
contribution_card = dbc.Card(
    [
        dbc.Col(html.H5(children = "Contribution:")),
        dbc.FormGroup(
            [
                    html.Div(id="output-contribution")
            ],
        )
    ],
    body=True
)

#App Layout
app.layout = dbc.Container(
    [
        #Title
        dbc.Row(
            dbc.Col(
                html.H2(children = "Loan Calculator")
            )
        ), 
        #Info from User
        dbc.Row(
            [
                dbc.Col(inputs, md=3),
            ],
            align="left",
        ),
        #Individual Section
        dbc.Row(
            [
                dbc.Col(individual_card, md=12),
            ],
        align="left",
        ),
        #Portfolio Section
        dbc.Row(
            [
                dbc.Col(port_card, md=12)
            ],
        align="left",
        ),
        #Portfolio Charts
        dbc.Row(
            [
                dbc.Col(
                    dcc.Graph(id='output-portfolio-bar')),
                dbc.Col(
                    dcc.Graph(id='output-portfolio-line'))
            ],
        align="left",
        ),
        #Contribution inputs
        dbc.Row(
            [
                dbc.Col(contributions_input, md =3),
            ],
        align="left",
        ),
        #Contribution Output
        dbc.Row(
            [
                dbc.Col(contribution_card, md=6),
                dbc.Col(
                    dcc.Graph(id='contribution-chart')
                ) 
            ],
        align="left",
        ),
    ],
    id="main-container",
    style={"display": "flex", "flex-direction": "column"},
    fluid=True
)

#Backend coding
#Created this function to round the numbers during schedule creation 
def roundedDict(schedule):
    rounded = {}
    foo = lambda t: (round(t[0],2), round(t[1],2), round(t[2],2), round(t[3],2), round(t[4],2), round(t[5],2), round(t[6],2))
    for k in schedule.keys():
        rounded[k] = foo(schedule[k])
    return rounded

@pytest.mark.parametrize('principal, rate, payment, extra_payment',
                         [
                             (5000.0, 6.0, 96.66, 0.0),
                             (10000.0, 8.0, 121.33, 0.0),
                             (7000.0, 7.0, 167.62, 0.0),
                         ])
def compute_schedule(principal, rate, payment, extra_payment):
    loans.schedule = {} 
    loan = None
    try:
        loan = Loan(principal=principal, rate=rate, payment=payment, extra_payment=extra_payment)
        loan.check_loan_parameters()
        loan.compute_schedule()
    except ValueError as ex:
        print(ex)
    #Add loans to portfolio and start aggregation
    loans.add_loan(loan)
    loans.aggregate()

def generate_table(dataframe, max_rows=25):
    return dash_table.DataTable(
            id='table',
            columns=[{'id': c, 'name': c} for c in dataframe.columns],
            data=dataframe.to_dict('records'),
            page_size = max_rows,
            fixed_rows={'headers': True},
            style_cell = {'minWidth': '25px', 'width': '80px', 'maxWidth': '85px'},
            style_table={'height': '300px',
                         'overflowY': 'auto',
                         'overflowX' : 'auto'}
            )

@app.callback(
    [Output("output-individual", "children"), Output("output-portfolio", "children")],
    [Input('buttonSearch', 'n_clicks')],
    [State("input1", "value"), State("input2", "value"),State("input3", "value"),State("input4", "value")]
)
def schedule_creation(n_clicks, input1, input2, input3, input4):
    port_table="no table"

    if (input1 == None or input2 == None or input3 == None or input4 == None):
        return None, None
    compute_schedule(round(float(input1),2), round(float(input2),2), round(float(input3),2), round(float(input4),2))

    # For Individual Loan Schedule(s)
    #loans.getloans = list of individual loans and we iterate through that
    indvloans = []
    for l in loans.getloans():
        tempdataframe = pd.DataFrame.from_dict(roundedDict(l.schedule), orient='index', columns=['Payment Number', 'Begin Principal', 'Payment', 'Extra Payment',
                   'Applied Principal', 'Applied Interest', 'End Principal'])
        indvloans.append(generate_table(tempdataframe)) #create table

    #For Portfolio Schedule   
    dataframe = pd.DataFrame.from_dict(roundedDict(loans.getportfolio()), orient='index', columns=['Payment Number', 'Begin Principal', 'Payment', 'Extra Payment',
                   'Applied Principal', 'Applied Interest', 'End Principal'])
    port_table = generate_table(dataframe)

    return indvloans, port_table

#graphs creation
@app.callback(
    [Output("output-portfolio-bar", "figure"), Output("output-portfolio-line", "figure")],
    [Input('buttonSearch', 'n_clicks')],
    [State("input1", "value"), State("input2", "value"),State("input3", "value"),State("input4", "value")]
) 
def port_chart(n_clicks, input1, input2, input3, input4):
    if (input1 == None or input2 == None or input3 == None or input4 == None):
        print(2)
        return go.Figure(), go.Figure()

    df= pd.DataFrame.from_dict(roundedDict(loans.getportfolio()), orient='index', columns=['Payment Number', 'Begin Principal', 'Payment', 'Extra Payment',
               'Applied Principal', 'Applied Interest', 'End Principal'])    
   
    #Stack Bar Chart
    fig = go.Figure(data = [
            go.Bar(name = "Applied Principal", 
                    x=df['Payment Number'], 
                    y= df['Applied Principal'],
                    marker_color ='indianred',
                    hovertext = df['Payment']),
            go.Bar(name='Applied Interest',
                    x=df['Payment Number'], 
                    y= df['Applied Interest'],
                    marker_color = 'lightsalmon',
                    hovertext = df['Payment'])
            ]
        )
    fig.update_layout(
          title='Portfolio Breakdown',
          xaxis=dict(
              title='Payment Number',
              titlefont_size=16,
          ),
          yaxis=dict(
              title='USD',
              titlefont_size=16,
          ),
        barmode='stack',
        height = 400
    )
    
    #Line Chart
    fig1 = px.line(df, x="Payment Number", y="End Principal") 
    fig1.update_layout(
        title='Portfolio End Principal Over Time',
          xaxis=dict(
              title='Payment Number',
              titlefont_size=16,
          ),
          yaxis=dict(
              title='USD',
              titlefont_size=16,
          ),
        plot_bgcolor="white",
        height = 400
    )

    return fig, fig1 

@app.callback(
    [Output("output-contribution", "children"), Output("contribution-chart", "figure")],
    [Input('buttonContributions', 'n_clicks')],
    [State("input1", "value"), State("input2", "value"),State("input3", "value"),State("input4", "value"),
     State("cont-1", "value"), State("cont-2", "value"),State("cont-3", "value"),State("cont-4", "value"),State("cont-5", "value")]
)
def impact_cont(n_clicks, input1, input2, input3, input4, cont1, cont2, cont3, cont4, cont5):
    if (input1 == None or input2 == None or input3 == None or input4 == None):
        print(3)
        return None, go.Figure()
    contributions = [float(cont1), float(cont2), float(cont3), float(cont4), float(cont5)]
    impacts = LoanImpacts(round(float(input1),2), round(float(input2),2), round(float(input3),2), round(float(input4),2), contributions)
    table = impacts.compute_impacts()
    dataframe = pd.DataFrame.from_dict(table, orient='index',
                                       columns=['Index', 'Interest Paid', 'Duration', 'MIInterest',
                                                'MIDuration'])
    cont_table = generate_table(dataframe)

    #Contribution Chart 
    fig = go.Figure()
    fig.add_trace(go.Bar(
        x=dataframe['Index'],
        y= dataframe['MIInterest'],
        marker_color='green'
        ))
    fig.update_layout(
        title='Contribution Breakdown',
        xaxis=dict(
            title='Who is Contributing?',
            titlefont_size=16,
            tickmode = 'array',
            tickvals = [0, 1, 2, 3, 4, 5],
            ticktext = ['Everyone', 'Everyone but P1', 'Everyone but P2', 'Everyone but P3', 'Everyone but P4', 'Everyone but P5'],
            tickfont_size=10
              ),
        yaxis=dict(
            title = 'Interest Saved',
            titlefont_size = 16, 
            tickfont_size=10
              ),
        barmode='group',
        bargap=0.15,
        bargroupgap=0.1, 
        height = 400
      )
    return cont_table, fig

if __name__ == '__main__':
    app.run_server(debug=True, use_reloader = False)




