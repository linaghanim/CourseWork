import plotly.graph_objects as go
import pandas as pd
import dash
import dash_core_components as dcc
import dash_html_components as html
from dash.dependencies import Input, Output, State
import csv
import m
#-------------------------

# route data
dfall = pd.read_csv('finaldata.csv')
dfall['text'] = dfall['Source_Airport'] + ', ' + dfall['Source_City']
external_stylesheets = ['https://codepen.io/chriddyp/pen/bWLwgP.css']
app = dash.Dash(__name__, external_stylesheets=external_stylesheets)

# city index data
city_df = pd.read_csv('dict.csv')
city_df.columns=['Index','City']

#save city index in dictionary
city_dic={}
with open('dict.csv', mode='r') as infile:
    reader = csv.reader(infile)
    city_dic = {rows[1]:int(rows[0]) for rows in reader}

#map figure
fig = go.Figure(data=go.Scattergeo(
        lon=dfall['Source_Longitude'],
        lat=dfall['Source_Latitude'],
        text=dfall['text'],
        mode='markers'
    ))
fig.update_layout(
    title='',
    geo_scope='usa'
)

#app layout
app.layout = html.Div(children=[
    html.Div(children=[
        html.H3(children='Kartemap', style={"color":'#EB5A01',"margin-left":'5%'}),
        html.H6(children='Find the shortest path', style={"color": '#274473', "margin-left": '1%', "margin-top":'20%'}),
        html.H6(children='From', style={"color": '#274473', "margin-left": '1%', "margin-top":'5%'}),

        dcc.Dropdown(
                    options=[{"label": col, "value": col} for col in city_df['City']],
                    value="Choose a start city",
                    id="input1",
        ),
        html.H6(children='To', style={"color": '#274473', "margin-left": '1%', "margin-top":'5%'}),

        dcc.Dropdown(
                    options=[{"label": col, "value": col} for col in city_df['City']],
                    value="Choose a destination city",
                    id="input2",
                ),
        html.Button(id='buttonSearch', n_clicks=0, children='Search',style={'margin-left':'1%',"margin-top":'5%',"background-color": '#274473', "color": 'white', 'border-radius': '100%'}),
        html.Button(id='buttonCancel', n_clicks=0, children='Cancel', style={'margin-left': '1px', "margin-top": '5%', "background-color": '#274473',
                           "color": 'white', 'border-radius': '100%'}),
        html.Div(id="output1", style={"margin-top":'5%', "color": '#274473', "margin-left": '1%'})],
        style={'width': '30%', 'display': 'inline-block', 'align': 'top', 'vertical-align': 'top'}),
        html.Div(
            dcc.Graph(
                id='output-map',
                figure=fig
            ),
            style={'width': '70%', 'display': 'inline-block', 'align': 'right', 'vertical-align': 'top'})
    ])

#input: origin and destination city index, Outpu: path
def get_path(fro, to): #cities
    #get city index by name
    index_fro=city_dic[fro]
    index_to=city_dic[to]

    #get list of city names from start to end
    start_end_path=m.main(index_fro,index_to)
    lons = []
    lats = []
    # get lon and lat of each city in shortest path
    for c in start_end_path:
        cityrow=dfall[dfall['Source_City'] == c] #find city row in original file
        if len(cityrow)==0:
            print("city name does not exist!")
            return 0, 0, 0
        cityrow=cityrow[:1]
        lons.extend(cityrow["Source_Longitude"])
        lats.extend(cityrow["Source_Latitude"])
    return start_end_path, lats, lons #return a list of long/lat of destinations in shortest path

@app.callback(
    [Output("output1", "children"), Output("output-map", "figure")],
    [Input('buttonSearch', 'n_clicks'), Input('buttonCancel', 'n_clicks')],
    [State("input1", "value"), State("input2", "value")]
)
def route_line(n_clicks1, n_clicks2, input1, input2):
    changed_id = [p['prop_id'] for p in dash.callback_context.triggered][0]
    if ('buttonCancel' in changed_id):
        return ["", fig]
    if ('buttonSearch' in changed_id):
        if input1==None or input2==None:
            return ["city name does not exist!", fig]
        path, lats, lons = get_path(input1, input2) #find shortest path
        if path==0:
            return ["city name does not exist!", fig]
        # draw route line
        fig2 = go.Figure(data=go.Scattergeo(
            lon=lons,
            lat=lats,
            mode='markers+lines'
        ))
        fig2.update_layout(
            title='',
            geo_scope='usa'
        )
        return [u'From {} To {} the Path is {}'.format(input1, input2, path) , fig2]
    else:
        return ["", fig]

if __name__ == '__main__':
    app.run_server(debug=True)

